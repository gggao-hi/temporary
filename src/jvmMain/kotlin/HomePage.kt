import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

private val dbHelper = DBHelper()
private val verifyHelper = VerifyHelper().apply {
    this.onVerifyResultListener = object : VerifyHelper.OnVerifyResultListener {
        override fun onAppVersionResult(result: List<String>) {
            combineString(result)?.apply {
                verifyAppVersionResult.value = this
            }
            appVersionVerified.value = true

        }

        override fun onRomVersionResult(result: List<String>) {
            combineString(result)?.apply {
                verifyRomVersionResult.value = this
            }
            romVersionVerified.value = true

        }

        override fun onDisabledFeaturesResult(result: List<String>) {
            combineString(result)?.apply {
                verifyDisabledFeatureResult.value = this
            }
            disabledFeaturesVerified.value = true
        }

        private fun combineString(result: List<String>): String? {
            return result.takeIf { it.isNotEmpty() }?.let { list ->
                val appResult = StringBuffer()
                list.forEach { item ->
                    appResult.append(item).append("\n")
                }
                appResult.toString()
            }
        }
    }
}

@Composable
fun HomePage() {
    Logger.init()
    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize(1.0f).padding(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(1.0f), horizontalArrangement = Arrangement.Center) {
                Button({
                    chooseDBFile()
                }) {
                    Text("load db file")
                }
                Spacer(Modifier.size(40.dp))
                Button({
                    verify()
                }) {
                    Text("verify")
                }
            }
            Text(
                "db file path: ${dbFilePath.value}",
                modifier = Modifier.fillMaxWidth(1.0f),
                fontSize = 20.0.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp).fillMaxWidth(1.0f))
            Text(
                "verify result:",
                modifier = Modifier.fillMaxWidth(1.0f),
                fontSize = 20.0.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp).fillMaxWidth(1.0f))
            Column(modifier = Modifier.fillMaxWidth(1.0f).verticalScroll(ScrollState(0))) {
                if (verifyAppVersionResult.value.isNotEmpty()) {
                    Text(
                        "App Version Verify Result:",
                        modifier = Modifier.fillMaxWidth(1.0f),
                        fontSize = 20.0.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        verifyAppVersionResult.value,
                        modifier = Modifier.fillMaxWidth(1.0f),
                        fontSize = 18.0.sp,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(10.dp).fillMaxWidth(1.0f))
                }
                if (verifyRomVersionResult.value.isNotEmpty()) {
                    Text(
                        "Rom Version Verify Result:",
                        modifier = Modifier.fillMaxWidth(1.0f),
                        fontSize = 20.0.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        verifyRomVersionResult.value,
                        modifier = Modifier.fillMaxWidth(1.0f),
                        fontSize = 18.0.sp,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(10.dp).fillMaxWidth(1.0f))
                }
                if (verifyDisabledFeatureResult.value.isNotEmpty()) {
                    Text(
                        "Disabled Features Verify Result:",
                        modifier = Modifier.fillMaxWidth(1.0f),
                        fontSize = 20.0.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        verifyDisabledFeatureResult.value,
                        modifier = Modifier.fillMaxWidth(1.0f),
                        fontSize = 18.0.sp,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(10.dp).fillMaxWidth(1.0f))
                }

                if (appVersionVerified.value && romVersionVerified.value && disabledFeaturesVerified.value
                    && verifyAppVersionResult.value.isEmpty() && verifyRomVersionResult.value.isEmpty()
                    && verifyDisabledFeatureResult.value.isEmpty()
                ) {
                    Text(
                        "Verify Pass ",
                        modifier = Modifier.fillMaxWidth(1.0f).align(Alignment.CenterHorizontally),
                        fontSize = 22.0.sp,
                        color = Color.Green,
                        fontWeight = FontWeight.Bold
                    )
                }

            }

        }
    }

}


private val verifyAppVersionResult: MutableState<String> = mutableStateOf("")
private val verifyRomVersionResult: MutableState<String> = mutableStateOf("")
private val verifyDisabledFeatureResult: MutableState<String> = mutableStateOf("")
private val appVersionVerified: MutableState<Boolean> = mutableStateOf(false)
private val romVersionVerified: MutableState<Boolean> = mutableStateOf(false)
private val disabledFeaturesVerified: MutableState<Boolean> = mutableStateOf(false)

private fun resetVerified() {
    appVersionVerified.value = false
    romVersionVerified.value = false
    disabledFeaturesVerified.value = false
}

private fun verify() {
    resetVerified()
    dbHelper.query(dbFilePath.value) {
        println("size: ${it.size}")
        verifyHelper.verify(it)

    }
}

private val dbFilePath: MutableState<String> = mutableStateOf("")
private fun chooseDBFile() {
    resetVerified()
    JFileChooser().apply {
        this.fileFilter = FileNameExtensionFilter(".db", "db")
        this.fileSelectionMode = JFileChooser.FILES_ONLY
        showDialog(ComposeWindow(), "ok")
        dbFilePath.value = selectedFile?.absolutePath ?: ""
    }
}

