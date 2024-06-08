package com.dataxing.indiapolls.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DefaultButton(
    topMargin: Dp,
    bottomMargin: Dp,
    text: String,
    onClick: () -> Unit,
) {
    DefaultButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = topMargin, bottom = bottomMargin)
            .height(52.dp), // Set height to 64dp
        text = text,
        onClick = onClick,
    )
}

@Composable
fun DefaultButton(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier, // Set height to 64dp
        shape = MaterialTheme.shapes.medium.copy(all = CornerSize(10.dp)), // Adjust corner radius
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = primary,
            contentColor = white
        )
    ) {
        Text(
            text = text,
            style = TextStyle(fontSize = 14.sp, color = white, fontFamily = displayFontFamily)
        )
    }
}

@Composable
fun SurveyOutlinedButton(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier, // Set height to 64dp
        shape = MaterialTheme.shapes.medium.copy(all = CornerSize(6.dp)), // Adjust corner radius
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = white,
            contentColor = black
        )
    ) {
        Text(
            text = text,
            style = TextStyle(fontSize = 14.sp, color = black, fontFamily = displayFontFamily)
        )
    }
}

@Composable
fun DefaultItemButton(
    modifier: Modifier,
    text: String,
    contentColor: Color,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier, // Set height to 64dp
        shape = MaterialTheme.shapes.medium.copy(all = CornerSize(10.dp)), // Adjust corner radius
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = contentColor,
            contentColor = white
        )
    ) {
        Text(
            text = text,
            style = TextStyle(fontSize = 14.sp, color = white, fontFamily = displayFontFamily)
        )
    }
}

@Composable
fun DefaultIconButton(
    topMargin: Dp,
    bottomMargin: Dp,
    buttonColors: ButtonColors,
    asset: Int,
    assetColor: Color,
    text: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = topMargin, bottom = bottomMargin)
            .height(52.dp), // Set height to 64dp
        shape = MaterialTheme.shapes.medium.copy(all = CornerSize(10.dp)), // Adjust corner radius
        onClick = onClick,
        colors = buttonColors
    ) {
        Icon(
            painter = painterResource(id = asset),
            contentDescription = "",
            modifier = Modifier.size(
                ButtonDefaults.IconSize
            ),
            tint = assetColor
        )
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = text,
            style = TextStyle(fontSize = 14.sp, color = white, fontFamily = displayFontFamily)
        )
    }
}

@Composable
fun DefaultOutlinedTextField(
    topMargin: Dp,
    text: String,
    onValueChange: (String) -> Unit,
    label: String,
    singleLine: Boolean,
    keyboardOptions: KeyboardOptions,
    isError: Boolean,
    errorText: String,
    minLines: Int = 1,
    maxLines: Int = 1,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = topMargin),
        value = text,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            )
        },
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        isError = isError,
        supportingText = {
            if (isError) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorText,
                    color = errorLight
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = textPrimary,
            unfocusedBorderColor = textPrimary,
            errorTextColor = errorLight,
            errorPlaceholderColor = errorLight,
            focusedTextColor = textPrimary,
            unfocusedTextColor = textPrimary,
            focusedLabelColor = textPrimary,
            unfocusedLabelColor = textPrimary,
            errorLabelColor = errorLight,
            cursorColor = primary
        ),
        keyboardOptions = keyboardOptions,
        textStyle = TextStyle(
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    )
}


@Composable
fun DefaultOutlinedTextFieldWithTrailingIcon(
    modifier: Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    label: String,
    singleLine: Boolean,
    keyboardOptions: KeyboardOptions,
    isError: Boolean,
    errorText: String,
    maxLines: Int = 1,
    readOnly: Boolean,
    supportingText: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable () -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        readOnly = readOnly,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            )
        },
        singleLine = singleLine,
        maxLines = maxLines,
        isError = isError,
        supportingText = supportingText,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = textPrimary,
            unfocusedBorderColor = textPrimary,
            errorTextColor = errorLight,
            errorPlaceholderColor = errorLight,
            focusedTextColor = textPrimary,
            unfocusedTextColor = textPrimary,
            focusedLabelColor = textPrimary,
            unfocusedLabelColor = textPrimary,
            errorLabelColor = errorLight,
            cursorColor = primary
        ),
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        textStyle = TextStyle(
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    )
}

@Composable
fun DefaultPasswordOutlinedTextField(
    topMargin: Dp,
    text: String,
    onValueChange: (String) -> Unit,
    label: String,
    singleLine: Boolean,
    keyboardOptions: KeyboardOptions,
    isError: Boolean,
    errorText: String
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = topMargin),
        value = text,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            )
        },
        singleLine = singleLine,
        isError = isError,
        supportingText = {
            if (isError) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorText,
                    color = errorLight
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = textPrimary,
            unfocusedBorderColor = textPrimary,
            errorTextColor = errorLight,
            errorPlaceholderColor = errorLight,
            focusedTextColor = textPrimary,
            unfocusedTextColor = textPrimary,
            focusedLabelColor = textPrimary,
            unfocusedLabelColor = textPrimary,
            errorLabelColor = errorLight,
            cursorColor = primary,
            errorTrailingIconColor = textPrimary,
        ),
        keyboardOptions = keyboardOptions,
        textStyle = TextStyle(
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        ),
        visualTransformation = if (passwordVisible)
            VisualTransformation.None
        else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            // Please provide localized description for accessibility services
            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
        }
    )
}

@Composable
fun DefaultOutlinedTextFieldWithIcon(
    modifier: Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    label: String,
    singleLine: Boolean,
    keyboardOptions: KeyboardOptions,
    isError: Boolean,
    readOnly: Boolean = false,
    enable: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    errorText: String,
    trailingImage: ImageVector,
    onTrailingIconClick: () -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            )
        },
        singleLine = singleLine,
        isError = isError,
        readOnly = readOnly,
        interactionSource = interactionSource,
        supportingText = {
            if (isError) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorText,
                    color = errorLight
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = textPrimary,
            unfocusedBorderColor = textPrimary,
            errorTextColor = errorLight,
            errorPlaceholderColor = errorLight,
            focusedTextColor = textPrimary,
            unfocusedTextColor = textPrimary,
            focusedLabelColor = textPrimary,
            unfocusedLabelColor = textPrimary,
            errorLabelColor = errorLight,
            cursorColor = primary,
            errorTrailingIconColor = textPrimary,
        ),
        keyboardOptions = keyboardOptions,
        textStyle = TextStyle(
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        ),
        trailingIcon = {
            IconButton(onClick = onTrailingIconClick) {
                Icon(imageVector = trailingImage, null)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultDropdownMenu(
    topMargin: Dp,
    text: String,
    onValueChange: (Int, String) -> Unit,
    onExpandedChange: ((Boolean) -> Unit)? = null,
    label: String,
    singleLine: Boolean,
    keyboardOptions: KeyboardOptions,
    isError: Boolean,
    errorText: String,
    options: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
            onExpandedChange?.invoke(expanded)
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(color = white)
            .padding(top = topMargin)
    ) {
        DefaultOutlinedTextFieldWithTrailingIcon(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            text = text,
            onValueChange = { },
            label = label,
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            isError = isError,
            errorText = errorText,
            maxLines = 1,
            supportingText = if (!expanded) {
                {
                    if (isError) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = errorText,
                            color = errorLight
                        )
                    }
                }
            } else {
                null
            }
        ) {
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
        }

        DropdownMenu(
            modifier = Modifier
            .background(color = white)
            .exposedDropdownSize(), expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEachIndexed { index: Int, option: String ->
                DropdownMenuItem(
                    text = {
                        Text(text = option, style = TextStyle(
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )) },
                    onClick = {
                        expanded = false
                        onValueChange(index, option)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}


