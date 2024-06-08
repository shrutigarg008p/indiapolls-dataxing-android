package com.dataxing.indiapolls.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.datastore.core.IOException
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.provider
import com.dataxing.indiapolls.ui.ViewModelFactory
import com.dataxing.indiapolls.ui.util.showMessage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date

class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by viewModels { ViewModelFactory() }

    private lateinit var cameraImagePath: String
    private var cameraImageUri: Uri? = null

    private val storagePermission = if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private val settingDialog: AlertDialog by lazy {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder
            .setTitle(R.string.permission_dialog_title)
            .setMessage(R.string.notification_permission_dialog_message)
            .setPositiveButton(R.string.Okay) { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", requireActivity().packageName, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

        builder.create()
    }

    private val selectImageDialog: AlertDialog by lazy {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder
            .setTitle(R.string.select_image_dialog_title)
            .setItems(R.array.select_image_array) { _, position ->
                if (position == 0) {
                    openCamera()
                } else {
                    openGallery()
                }
            }

        builder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ProfileScreen(context, findNavController(), ::askStoragePermission, ::logout,  profileViewModel)
            }
        }
    }

    private fun logout() {
        viewLifecycleOwner.lifecycleScope.launch {
            profileViewModel.logout()
            Firebase.auth.signOut()
            findNavController().navigate(R.id.mainActivity)
            activity?.finish()
        }
    }

    private fun askStoragePermission() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            val result = ContextCompat.checkSelfPermission(requireContext(), storagePermission)
            if (result == PackageManager.PERMISSION_GRANTED
            ) {
                selectImageDialog.show()
            } else if (shouldShowRequestPermissionRationale(storagePermission)) {
                requestPermissionLauncher.launch(storagePermission)
            } else {
                requestPermissionLauncher.launch(storagePermission)
            }
        } else {
            selectImageDialog.show()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            selectImageDialog.show()
        } else {
            settingDialog.show()
        }
    }

    private fun openCamera() {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            context.showMessage(requireContext().resources.getString(R.string.image_path_error))
            null
        }
        photoFile?.also {
            cameraImageUri = FileProvider.getUriForFile(
                requireContext(),
                provider,
                it
            )
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri)
            cameraActivityResultLauncher.launch(cameraIntent)
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            cameraImagePath = absolutePath
        }
    }

    private var cameraActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { it ->
        if (it.resultCode == Activity.RESULT_OK) {
            cameraImageUri?.let {
                uploadImage(it, cameraImagePath)
            }
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryActivityResultLauncher.launch(galleryIntent)
    }

    private var galleryActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.data?.let { uri ->
                getRealPathFromURI(uri, requireContext())?.let { path ->
                    uploadImage(uri, path)
                }
            }
        }
    }

    private fun getRealPathFromURI(uri: Uri, context: Context): String? {
        context.contentResolver.query(uri, null, null, null, null)?.let {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            val name = it.getString(nameIndex)
            val file = File(context.filesDir, name)
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val outputStream = FileOutputStream(file)
                var read = 0
                val maxBufferSize = 1 * 1024 * 1024
                val bytesAvailable: Int = inputStream?.available() ?: 0
                val bufferSize = Math.min(bytesAvailable, maxBufferSize)
                val buffers = ByteArray(bufferSize)
                while (inputStream?.read(buffers).also {
                        if (it != null) {
                            read = it
                        }
                    } != -1) {
                    outputStream.write(buffers, 0, read)
                }
                inputStream?.close()
                outputStream.close()

            } catch (e: java.lang.Exception) {
                context.showMessage(context.resources.getString(R.string.image_path_error))
            } finally {
                it.close()
            }
            return file.path
        }
        return null
    }

    private fun uploadImage(uri: Uri, path: String) {
        val file = File(path)
        profileViewModel.uploadPicture(file)
    }

    override fun onStart() {
        super.onStart()

        profileViewModel.fetchProfile()
    }
}