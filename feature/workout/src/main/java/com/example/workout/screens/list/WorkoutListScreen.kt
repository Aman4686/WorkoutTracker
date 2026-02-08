package com.example.workout.screens.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun WorkoutListScreen(viewModel: WorkoutListViewModel = hiltViewModel()) {
    WorkoutListScreenView()
}

@Composable
fun WorkoutListScreenView() {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(4) {
                WorkoutListItem()
            }
        }
        AddFloatingButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

@Composable
fun AddFloatingButton(modifier: Modifier){
    FloatingActionButton(
        onClick = {

        },
        modifier = modifier,
        shape = ShapeDefaults.Medium
    ) {
        Icon(Icons.Filled.Add, "Floating action button.")
    }
}

@Composable
fun WorkoutListItem() {
    Row(modifier = Modifier
        .padding(6.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(Color.LightGray)
        .fillMaxWidth()
        .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text("Training name", fontSize = 24.sp)
        Text("Date", fontSize = 24.sp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WorkoutListScreenPreview() {
    WorkoutListScreenView()
}