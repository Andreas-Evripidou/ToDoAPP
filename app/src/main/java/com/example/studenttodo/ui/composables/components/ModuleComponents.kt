package com.example.studenttodo.ui.composables.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studenttodo.entities.ModuleEntity
import com.example.studenttodo.ui.composables.displayTodo
import com.example.studenttodo.viewmodels.CreateViewModel

/*
* Composable function to display
* Module options
* */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectOrCreateModule(updateSelectedModuleCode: (mc: String) -> Unit, rowModifier: Modifier = Modifier, moduleCode: String = "") {
    val modules by viewModel<CreateViewModel>().modules.collectAsState(initial = emptyList())
    val openCreateModuleDialog = remember { mutableStateOf(false)  }
    val openEditModuleDialog = remember { mutableStateOf(false)  }
    val moduleTitles = makeArrayOfModuleCodes(modules)

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember {mutableStateOf(moduleCode)}

    fun updateCreatedModule(mc: String){
        selectedText = mc
        updateSelectedModuleCode(mc)
    }

    if (openCreateModuleDialog.value){
        ModuleCreateDialog(
            openDialog = openCreateModuleDialog,
            updateSelectedModuleCode = ::updateCreatedModule)
    }

    if (openEditModuleDialog.value){
        ModuleEditDialog(openDialog = openEditModuleDialog)
    }


    Row (modifier = Modifier.fillMaxHeight()) {
        Text(
            "Module Code:", modifier = Modifier,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(text = " *", color = MaterialTheme.colorScheme.error)
    }


    Row(modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically)
    {

        if (modules.isNotEmpty()) {
            if (selectedText == "" ){
                selectedText = moduleTitles[0]
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
        IconButton(onClick = { openCreateModuleDialog.value = true },
            modifier = Modifier
                .weight(0.2f)
                .fillMaxHeight()) {
            Icon(
                Icons.Filled.AddCircle,
                contentDescription = "Add Module",
                Modifier.fillMaxSize()
            )
        }
        IconButton(onClick = { openEditModuleDialog.value = true },
            modifier = Modifier
                .weight(0.2f)
                .fillMaxHeight()) {
            Icon(
                Icons.Filled.Edit,
                contentDescription = "Add Module",
                Modifier.fillMaxSize()
            )
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
fun ModuleCreateDialog(openDialog: MutableState<Boolean>, updateSelectedModuleCode: (mc: String) -> Unit) {
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
                updateSelectedModuleCode(code)
                openDialog.value = false
            }) {
                Text(text = "Create Module")
            }
        })
}

/*
* Composable function to display
* Edit Modules dialog
* */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ModuleEditDialog(openDialog: MutableState<Boolean>) {
    val viewModel = viewModel<CreateViewModel>()
    val modules by viewModel<CreateViewModel>().modules.collectAsState(initial = emptyList())

    AlertDialog(
        title = { Text(text = "Edit Modules")},
        text = {
            Column  (modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            )
            {
                Log.d("MODULE", "${modules.size}")
                modules.forEach { module ->
                    Log.d("MODULE","${module.moduleCode}")
                    displayModule(
                        module = module,
                        onDelete = {viewModel.deleteModule(module)}
                    )
                }
            }
        },
        onDismissRequest = { openDialog.value = false },
        dismissButton = {
            Button(onClick = { openDialog.value = false })
            {
                Text(text = "Dismiss")
            }
        }
        ,
        confirmButton = {
            Button(onClick = {
                openDialog.value = false
            }) {
                Text(text = "OK")

            }
        })
}

@Composable
fun displayModule(module: ModuleEntity, onDelete: (ModuleEntity) -> Unit) {

    val openDeleteAlertDialog = remember { mutableStateOf(false)  }

    if (openDeleteAlertDialog.value) {
        onDeleteAlert(onDelete = onDelete, module = module, openDialog = openDeleteAlertDialog)
    }


    Card (modifier = Modifier
        .padding(bottom = 10.dp)
    ) {
        Row (modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Box(modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
            ) {
                Column (modifier = Modifier.padding(start = 6.dp) ){
                    Text(text = module.moduleCode, style = MaterialTheme.typography.headlineSmall)
                    Text(text = "Module Title: ${module.moduleTitle}", style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.size(16.dp))

            Box(modifier = Modifier
                .width(50.dp)
                .fillMaxHeight()
                .clickable { openDeleteAlertDialog.value = true }
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Done", Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun onDeleteAlert(onDelete: (ModuleEntity) -> Unit, module: ModuleEntity, openDialog: MutableState<Boolean>) {
    AlertDialog(
        title = {Text(text = "Delete Module")},
        text = {
            Column {
                Text(text = "Are you sure you want to permanent delete this Module?")
                Text(text = "This action will also Delete all the Schedule and Todo items associated with this Module")
            }
               },
        onDismissRequest = {openDialog.value = false},
        dismissButton = {
            Button(onClick = { openDialog.value = false})
            {
                Text(text = "Dismiss")
            }
        },
        confirmButton = {
            Button(onClick = {
                openDialog.value = false
                onDelete(module) },
                colors = ButtonDefaults.buttonColors( containerColor = Color.Red)
            )
            {
                Text(text = "Delete")
            }
        }
    )

}