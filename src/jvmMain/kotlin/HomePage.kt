import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun HomePage() {
    Theme {
        Column {
//            toast()
//            insertCaseDialog()
            if (newVersionDialogVisible.value) {
                versionDialog()
            }
            if (configDialogVisible.value) {
                configDialog()
            }

            Column(modifier = Modifier.weight(1.0f)) {
                projectSelector()
                featureFlagList()
            }

            bottomButton()
        }


    }
}

private val projectState = mutableStateOf("")
private val newVersionDialogVisible = mutableStateOf(false)
private val configDialogVisible = mutableStateOf(false)
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
            newVersionDialogVisible.value = true
        }, enabled = projectState.value.isNotBlank()) {
            Text("add")

        }
        TextButton(colors = ButtonDefaults.textButtonColors(Color.Blue, Color.White, Color.Gray), onClick = {

        }, enabled = projectState.value.isNotBlank()) {
            Text("test")

        }
        TextButton(colors = ButtonDefaults.textButtonColors(Color.Blue, Color.White, Color.Gray), onClick = {

        }, enabled = projectState.value.isNotBlank()) {
            Text("upload")

        }
        TextButton(colors = ButtonDefaults.textButtonColors(Color.Blue, Color.White, Color.Gray), onClick = {
            configDialogVisible.value = true
        }, enabled = projectState.value.isNotBlank()) {
            Text("config")

        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun versionDialog() {
    if (newVersionDialogVisible.value) {
        AlertDialog(onDismissRequest = {},
            title = {
                Text("Add New Versions", fontWeight = FontWeight.Bold)
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
                    TextButton(onClick = { newVersionDialogVisible.value = false }) {
                        Text("cancel")
                    }
                    TextButton(onClick = {
                        newVersionDialogVisible.value = false
                    }) {
                        Text("sure")
                    }
                }
            })
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun configDialog() {

    AlertDialog(onDismissRequest = {},
        title = {
            Text("Config", fontWeight = FontWeight.Bold)
        }, text = {
            Box(modifier = Modifier.size(300.dp, 300.dp)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxSize().padding(vertical = 20.dp)
                ) {
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(Color.Blue, Color.White, Color.Gray),
                        onClick = {

                        }) {
                        Text("unit case", maxLines = 1)

                    }
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(Color.Blue, Color.White, Color.Gray),
                        onClick = {

                        }) {
                        Text("feature flag status type", maxLines = 1)

                    }
                }
            }
        },
        buttons = {
            Row {
                TextButton(onClick = { configDialogVisible.value = false }) {
                    Text("cancel")
                }
                TextButton(onClick = {
                    configDialogVisible.value = false
                }) {
                    Text("sure")
                }
            }
        })

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun toast() {
    AlertDialog(onDismissRequest = {},
        title = {
            Text("Tips", fontWeight = FontWeight.Bold)
        }, text = {
            Text(
                "您本次指定了romVersion:100, appVersion:123,\n若仅适用于 100=device_romVersion and 123=device_romVersion的情况,\n则需要插入一条屏蔽 100<device_romVersion and 123<device_romVersion 情况的数据.",
                modifier = Modifier.width(400.dp), textAlign = TextAlign.Start, fontWeight = FontWeight.SemiBold
            )
        },
        buttons = {
            Row {
                TextButton(onClick = { configDialogVisible.value = false }) {
                    Text("no need")
                }
                TextButton(onClick = { configDialogVisible.value = false }) {
                    Text("insert")
                }
            }

        })
}

@OptIn(ExperimentalMaterialApi::class)

@Composable
private fun insertCaseDialog() {
    AlertDialog(onDismissRequest = {},
        title = {
            Text("Add Case", fontWeight = FontWeight.Bold)
        }, text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Spacer(Modifier.size(10.dp))
                TextField("", onValueChange = {}, label = { Text("device_romVersion") })
                Spacer(Modifier.size(10.dp))
                TextField("", onValueChange = {}, label = { Text("device_appVersion") })
            }
        },
        buttons = {
            Row {
                TextButton(onClick = { newVersionDialogVisible.value = false }) {
                    Text("Cancel")
                }
                TextButton(onClick = {
                    newVersionDialogVisible.value = false
                }) {
                    Text("Create")
                }
            }
        })

}


