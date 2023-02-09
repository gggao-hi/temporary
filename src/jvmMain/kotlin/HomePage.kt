import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun HomePage() {
    Theme {
        Column {
            addDataDialog()
            Column(modifier = Modifier.weight(1.0f)) {
                projectSelector()
                featureFlagList()
            }

            bottomButton()
        }


    }
}

private val projectState = mutableStateOf("")
private val dialogVisible = mutableStateOf(false)
private val projects: List<String> = listOf("Mulan_6125", "Mulan_6125F")
private val featureFlagDatas: Map<String, List<FeatureFlagStatusCollection>> = mapOf(
    Pair(
        "Mulan_6125", listOf(
            FeatureFlagStatusCollection(
                0, 0, FeatureFlagStatus(false, MapStreamingAndADMStatus.Open), FeatureFlagStatus(false, true)
            ), FeatureFlagStatusCollection(
                100, 0, FeatureFlagStatus(false, MapStreamingAndADMStatus.Open), FeatureFlagStatus(false, false)
            ), FeatureFlagStatusCollection(
                0, 123, FeatureFlagStatus(false, MapStreamingAndADMStatus.Close), FeatureFlagStatus(false, true)
            ), FeatureFlagStatusCollection(
                100, 123, FeatureFlagStatus(false, MapStreamingAndADMStatus.Close), FeatureFlagStatus(false, false)
            )
        )
    )
)


@Composable
private fun featureFlagList() {
    projectState.let {
        featureFlagDatas[it.value]
    }?.also { data ->
        LazyVerticalGrid(columns = GridCells.Adaptive(160.dp), contentPadding = PaddingValues(10.dp)) {
            println("${data.size}")
            items(data) {

                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("romVersion: ${it.romVersion}", modifier = Modifier.padding(5.dp))
                    Text("appVersion: ${it.appVersion}", modifier = Modifier.padding(5.dp))
                }
            }
        }
    }

}

@Composable
private fun projectSelector() {
    var selectedProject: String by remember {
        mutableStateOf("select project")
    }
    var expended: Boolean by remember {
        mutableStateOf(false)
    }
    Row {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp).weight(3.0f)
        ) {
            Text("project:")
            Box {
                TextButton(onClick = {
                    expended = true
                }, content = {
                    Text(selectedProject)
                })
                DropdownMenu(expanded = expended, onDismissRequest = {}) {
                    projects.forEach {
                        DropdownMenuItem(onClick = {
                            selectedProject = it

                            expended = false
                        }) {
                            Text(it)
                        }
                    }

                }
            }

        }
        Box(modifier = Modifier.weight(1.0f)) {
            TextButton(colors = ButtonDefaults.textButtonColors(Color.Blue, Color.White, Color.Gray),
                modifier = Modifier.padding(20.dp, 0.dp),
                onClick = {
                    projectState.value = selectedProject
                }) {
                Text("download")

            }
        }

    }
}

@Composable
private fun bottomButton() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 0.dp, vertical = 10.dp)
    ) {
        TextButton(colors = ButtonDefaults.textButtonColors(Color.Blue, Color.White, Color.Gray), onClick = {
            dialogVisible.value = true
        }) {
            Text("add")

        }
        TextButton(colors = ButtonDefaults.textButtonColors(Color.Blue, Color.White, Color.Gray), onClick = {

        }) {
            Text("test")

        }
        TextButton(colors = ButtonDefaults.textButtonColors(Color.Blue, Color.White, Color.Gray), onClick = {

        }) {
            Text("upload")

        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun addDataDialog() {
    if (dialogVisible.value) {
        AlertDialog(onDismissRequest = {},
            title = {
                Text("Add New Versions")
            }, text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Spacer(Modifier.size(10.dp))
                    TextField("", onValueChange = {}, label = { Text("romVersion") })
                    Spacer(Modifier.size(10.dp))
                    TextField("", onValueChange = {}, label = { Text("appVersion") })
                }
            },
            buttons = {
                Row {
                    TextButton(onClick = { dialogVisible.value = false }) {
                        Text("cancel")
                    }
                    TextButton(onClick = { dialogVisible.value = false
                    }) {
                        Text("sure")
                    }
                }
            })
    }
}