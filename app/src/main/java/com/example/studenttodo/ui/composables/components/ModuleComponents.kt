package com.example.studenttodo.ui.composables.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.entities.ModuleEntity
import com.example.studenttodo.viewmodels.CreateViewModel

/*
* Composable function to display
* Module options
* */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectOrCreateModule(openDialog: (open: Boolean) -> Unit, updateSelectedModuleCode: (mc: String) -> Unit, rowModifier: Modifier = Modifier, moduleCode: String = "") {
    Column {

        Row {
            Text(
                "Module Code:", modifier = Modifier,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(text = " *", color = MaterialTheme.colorScheme.error)
        }


        Row(
            modifier = rowModifier,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            val modules by viewModel<CreateViewModel>().modules.collectAsState(initial = emptyList())
            if (modules.isNotEmpty()) {
                val moduleTitles = makeArrayOfModuleCodes(modules)

                var expanded by remember { mutableStateOf(false) }
                var selectedText by remember { mutableStateOf(moduleTitles[0]) }
                if (moduleCode != "" && moduleCode in moduleTitles) {
                    selectedText = moduleCode
                } else {
                    updateSelectedModuleCode(selectedText)
                }


                ExposedDropdownMenuBox(
                    modifier = Modifier.weight(0.8f),
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }) {
                    TextField(
                        value = selectedText,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }) {
                        moduleTitles.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    selectedText = item
                                    expanded = false
                                    updateSelectedModuleCode(selectedText)
                                    //Toast.makeText(context, item, Toast.LENGTH_SHORT)
                                })
                        }
                    }
                }
            } else{
                Text(text = "No modules found",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.error)
            }
            IconButton(onClick = { openDialog(true) },
                modifier = Modifier.weight(0.2f).fillMaxHeight()) {
                Icon(
                    Icons.Filled.AddCircle,
                    contentDescription = "Add Module",
                    Modifier.fillMaxSize()
                )
            }
        }
    }
}

fun makeArrayOfModuleCodes(modules: List<ModuleEntity>) : ArrayList<String> {
    val moduleCodes = ArrayList<String>()
    for (module in modules) {
        moduleCodes.add(module.moduleCode)
    }

    return moduleCodes
}

/*
* Composable function to display
* Create Module dialog
* */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ModuleCreateDialog(openDialog: MutableState<Boolean>) {
    val viewModel = viewModel<CreateViewModel>()
    var code by remember { mutableStateOf("") }
    var moduleTitle by remember { mutableStateOf("") }


    AlertDialog(
        title = { Text(text = "Create Module")},
        text = {
            Column (verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "Module Title", modifier = Modifier,
                    style = MaterialTheme.typography.headlineSmall)
                TextField(
                    value = moduleTitle,
                    onValueChange = { moduleTitle = it },
                    label = { Text(text = "Module Title") })

                Spacer(modifier = Modifier.size(10.dp))

                Text(text = "Module Code", modifier = Modifier,
                    style = MaterialTheme.typography.headlineSmall)
                TextField(
                    value = code,
                    onValueChange = { code = it.uppercase() },
                    label = { Text(text = "Module Code") })
                Spacer(modifier = Modifier.size(10.dp))

            }
        },
        onDismissRequest = { openDialog.value = false },
        dismissButton = {
            Button(onClick = { openDialog.value = false })
            {
                Text(text = "Dismiss")
            }
        },
        confirmButton = {
            Button(onClick = {
                val module = ModuleEntity(
                    moduleCode = code,
                    moduleTitle = moduleTitle
                )
                viewModel.createModule(module = module)
                openDialog.value = false
            }) {
                Text(text = "Create Module")
            }
        })
}
