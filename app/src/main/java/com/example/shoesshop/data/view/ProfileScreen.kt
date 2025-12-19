package com.example.shoesshop.data.view

import android.Manifest
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import com.example.shoesshop.R
import com.example.shoesshop.data.viewModel.ProfileUiState
import com.example.shoesshop.data.viewModel.ProfileViewModel
import com.example.shoesshop.ui.components.RegisterButton
import com.example.shoesshop.ui.theme.Typography
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState: ProfileUiState by viewModel.uiState.collectAsStateWithLifecycle()

    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var tempPhotoFile by remember { mutableStateOf<File?>(null) }

    // первый запуск – загрузить профиль
    LaunchedEffect(Unit) {
        viewModel.loadProfile(context)
    }

    // когда профиль пришёл – заполнить поля
    LaunchedEffect(uiState.profile) {
        uiState.profile?.let { p ->
            name = p.firstname ?: ""
            lastName = p.lastname ?: ""
            address = p.address ?: ""
            phone = p.phone ?: ""
        }
    }

    // проверка изменений относительно текущего профиля
    val hasChanges by remember(name, lastName, address, phone, selectedImageUri, uiState.profile) {
        derivedStateOf {
            val p = uiState.profile
            name != (p?.firstname ?: "") ||
                    lastName != (p?.lastname ?: "") ||
                    address != (p?.address ?: "") ||
                    phone != (p?.phone ?: "")
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isSuccess ->
            if (isSuccess) {
                tempPhotoFile?.let { file ->
                    selectedImageUri = Uri.fromFile(file)
                    Toast.makeText(context, "Фото сохранено", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Ошибка при съёмке фото", Toast.LENGTH_SHORT).show()
            }
            tempPhotoFile = null
        }
    )

    fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(Date())
        val storageDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        if (storageDirectory != null && !storageDirectory.exists()) {
            storageDirectory.mkdirs()
        }

        Log.d("PHOTO_DEBUG", "Директория: ${storageDirectory?.absolutePath}")
        Log.d("PHOTO_DEBUG", "Директория существует: ${storageDirectory?.exists()}")

        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDirectory
        ).apply {
            createNewFile()
            Log.d("PHOTO_DEBUG", "Файл создан: $absolutePath")
            Log.d("PHOTO_DEBUG", "Файл существует: ${exists()}")
        }
    }

    fun openCamera() {
        try {
            val photoFile = createImageFile()
            tempPhotoFile = photoFile
            val photoUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                photoFile
            )
            cameraLauncher.launch(photoUri)
        } catch (e: Exception) {
            Toast.makeText(context, "Ошибка при съёмке фото: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(context, "Нет разрешения на использование камеры", Toast.LENGTH_SHORT).show()
            }
        }
    )

    fun checkCameraPermissionAndOpen() {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Верхняя часть с заголовком и кнопкой редактирования
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.size(40.dp))
                Text(
                    text = stringResource(id = R.string.profile),
                    style = Typography.titleSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = {
                        if (isEditing) {
                            // откат к данным из профиля
                            uiState.profile?.let { p ->
                                name = p.firstname ?: ""
                                lastName = p.lastname ?: ""
                                address = p.address ?: ""
                                phone = p.phone ?: ""
                            }
                        }
                        isEditing = !isEditing
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(colorResource(R.color.Accent))
                            .size(25.dp)
                            .padding(4.dp),
                        painter = painterResource(id = R.drawable.edit),
                        contentDescription = if (isEditing) "Отмена" else "Редактировать",
                        tint = colorResource(R.color.white)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Аватар по центру
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0E0))
                ) {
                    if (selectedImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(selectedImageUri),
                            contentDescription = "Profile photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (name.isNotEmpty() || lastName.isNotEmpty())
                        "$name $lastName"
                    else
                        stringResource(R.string.profile),
                    style = Typography.bodyMedium
                )
            }

            if (isEditing) {
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { checkCameraPermissionAndOpen() },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colorResource(R.color.Accent)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.change_picture),
                        color = colorResource(R.color.Accent),
                        style = Typography.displaySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(35.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isEditing) {
                    EditableField(
                        label = stringResource(id = R.string.your_name),
                        value = name,
                        onValueChange = { name = it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    EditableField(
                        label = stringResource(id = R.string.lastname),
                        value = lastName,
                        onValueChange = { lastName = it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    EditableField(
                        label = stringResource(id = R.string.address),
                        value = address,
                        onValueChange = { address = it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    EditableField(
                        label = stringResource(id = R.string.phone),
                        value = phone,
                        onValueChange = { phone = it }
                    )
                } else {
                    InputField(label = stringResource(id = R.string.your_name), value = name)
                    Spacer(modifier = Modifier.height(16.dp))
                    InputField(label = stringResource(id = R.string.lastname), value = lastName)
                    Spacer(modifier = Modifier.height(16.dp))
                    InputField(label = stringResource(id = R.string.address), value = address)
                    Spacer(modifier = Modifier.height(16.dp))
                    InputField(label = stringResource(id = R.string.phone), value = phone)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (isEditing) {
                RegisterButton(
                    text = stringResource(R.string.save_now),
                    onClick = {
                        viewModel.saveProfile(
                            context = context,
                            firstname = name,
                            lastname = lastName,
                            address = address,
                            phone = phone
                        )
                        isEditing = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = hasChanges
                )
            }
        }
    }
}

@Composable
private fun InputField(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = Typography.labelMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFF5F5F5),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = if (value.isNotEmpty()) value else "Не указано",
                    style = Typography.bodySmall.copy(
                        color = if (value.isNotEmpty()) Color.Black else Color.Gray
                    )
                )
            }
        }
    }
}

@Composable
private fun EditableField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = Typography.labelMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = Typography.bodySmall,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF6200EE),
                unfocusedBorderColor = Color(0xFFE0E0E0)
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
