package com.firman.directoryuser.feature.user.presentation.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserScreen(
    onBackClick: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: AddUserViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    val isFormComplete by remember {
        derivedStateOf {
            state.name.isNotBlank() &&
            state.email.isNotBlank() &&
            state.emailError == null &&
            state.phoneNumber.isNotBlank() &&
            state.address.isNotBlank() &&
            state.city.isNotBlank()
        }
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onSuccess()
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Add New User", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .navigationBarsPadding()
            ) {
                HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = onBackClick,
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.extraLarge,
                        enabled = !state.isLoading
                    ) {
                        Text("Cancel", fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = viewModel::saveUser,
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.extraLarge,
                        enabled = !state.isLoading && isFormComplete
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Text("Save User", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !state.isLoading
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = state.emailError != null,
                supportingText = {
                    state.emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                enabled = !state.isLoading
            )

            OutlinedTextField(
                value = state.phoneNumber,
                onValueChange = viewModel::onPhoneChange,
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                enabled = !state.isLoading
            )

            OutlinedTextField(
                value = state.address,
                onValueChange = viewModel::onAddressChange,
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                enabled = !state.isLoading
            )

            CityDropdown(
                cities = state.cities,
                selectedCity = state.city,
                onCitySelected = viewModel::onCityChange,
                isLoading = state.isLoading
            )

            Column {
                Text(
                    text = "Gender",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GenderOption(
                        label = "Male",
                        isSelected = state.gender == 0,
                        onClick = { viewModel.onGenderChange(0) },
                        modifier = Modifier.weight(1f),
                        enabled = !state.isLoading
                    )
                    GenderOption(
                        label = "Female",
                        isSelected = state.gender == 1,
                        onClick = { viewModel.onGenderChange(1) },
                        modifier = Modifier.weight(1f),
                        enabled = !state.isLoading
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CityDropdown(
    cities: List<String>,
    selectedCity: String,
    onCitySelected: (String) -> Unit,
    isLoading: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (!isLoading) expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedCity,
            onValueChange = {},
            readOnly = true,
            label = { Text("City") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            enabled = !isLoading
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            cities.forEach { city ->
                DropdownMenuItem(
                    text = { Text(city) },
                    onClick = {
                        onCitySelected(city)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun GenderOption(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(48.dp),
        shape = MaterialTheme.shapes.extraLarge,
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = label,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
