package com.example.projetofinal.screens

import android.util.Patterns
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.projetofinal.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.rememberCoroutineScope
import com.example.projetofinal.data.User
import com.example.projetofinal.data.UserDatabase
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController, paddingValues: PaddingValues, database: UserDatabase, onLoginSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("")}
    var password by remember {mutableStateOf("")}
    var confirmPassword by remember {mutableStateOf("")}
    var passwordVisible by remember {mutableStateOf(false)}
    var confirmPasswordVisible by remember {mutableStateOf(false)}

    var nameError by remember {mutableStateOf("")}
    var emailError by remember {mutableStateOf("")}
    var passwordError by remember {mutableStateOf("")}
    var confirmPasswordError by remember {mutableStateOf("")}

    val coroutineScope = rememberCoroutineScope()

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.register_animation))

    val progress by animateLottieCompositionAsState(
        isPlaying = true,
        composition = composition,
        iterations = LottieConstants.IterateForever,
        speed = 0.7f
    )

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.CenterHorizontally),
            composition = composition,
            progress = {progress}
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Criar uma nova conta",
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth().padding(start = 25.dp, top = 15.dp),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.ExtraBold
        )

        Text(text = "Favor preencher as informações",
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth().padding(start = 25.dp), fontWeight = FontWeight.Light
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = name,
            onValueChange = {name = it},
            label = { Text(nameError.ifEmpty{"Nome"}, color = if (nameError.isNotEmpty()) Color.Red else Color.Unspecified)},
            leadingIcon = {
                Icon(Icons.Rounded.Person, contentDescription = "")
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.width(500.dp).padding(vertical = 4.dp, horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(8.dp))



        TextField(
            value = email,
            onValueChange = {email = it},
            label = { Text(emailError.ifEmpty{"E-mail"}, color = if (emailError.isNotEmpty()) Color.Red else Color.Unspecified)},
            leadingIcon = {
                Icon(Icons.Rounded.AccountCircle, contentDescription = "")
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.width(500.dp).padding(vertical = 4.dp, horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = {password = it},
            label = { Text(passwordError.ifEmpty{"Senha"}, color = if (passwordError.isNotEmpty()) Color.Red else Color.Unspecified)},
            leadingIcon = {
                Icon(Icons.Rounded.Lock, contentDescription = "")
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                Icon(
                    imageVector = image,
                    contentDescription = "",
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                )
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                        .width(500.dp)
                        .padding(vertical = 4.dp, horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = confirmPassword,
            onValueChange = {confirmPassword = it},
            label = { Text(confirmPasswordError.ifEmpty{"Confirmar senha"}, color = if (confirmPasswordError.isNotEmpty()) Color.Red else Color.Unspecified)},
            leadingIcon = {
                Icon(Icons.Rounded.Lock, contentDescription = "")
            },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                    Icon(
                        imageVector = image,
                        contentDescription = "",
                        modifier = Modifier.clickable { confirmPasswordVisible = !confirmPasswordVisible}
                    )
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .width(500.dp)
                .padding(vertical = 4.dp, horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                nameError = if (name.isBlank()) "Nome é obrigatório" else ""
                emailError = if (email.isBlank()) {
                    "E-mail é obrigatório"
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    "E-mail inválido"
                } else {
                    ""
                }
                passwordError = when {
                    password.isBlank() -> "Senha é obrigatória"
                    password.length < 8 -> "Senha deve ter pelo menos 8 caracteres"
                    else -> ""
                }
                confirmPasswordError = when {
                    confirmPassword.isBlank() -> "Confirma senha é obrigatória"
                    password != confirmPassword -> "Senhas não coincidem"
                    else -> ""
                }

                if (nameError.isEmpty() && emailError.isEmpty() && passwordError.isEmpty() && confirmPasswordError.isEmpty()) {
                    coroutineScope.launch {
                        val userDao = database.userDao()
                        val existingUser = userDao.getUserByEmail(email)

                        if (existingUser != null) {
                            emailError = "Este e-mail já está cadastrado"
                        } else {
                            val newUser = User(name = name, email = email, password = password)
                            userDao.insertUser(newUser)
                            navController.navigate("login")
                        }
                    }
                }
            }
        ) {
            Text("Cadastre-se")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = {navController.navigate("login")}) {
            Text("Tem uma conta? Conecte-se")
        }
    }
}







