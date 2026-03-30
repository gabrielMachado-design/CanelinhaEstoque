package com.example.canelinhaestoque.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onEstoqueClick: () -> Unit
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            MenuCard("Estoque"){

                onEstoqueClick()

            }
            MenuCard("Vendas"){

            }

        }

        Spacer(modifier =
        Modifier.height(24.dp))

        Row (

            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly

        ){

            MenuCard("Relatório Estoque") {


            }
            MenuCard("Relatório Vendas") {


            }

        }

    }

}

@Composable
fun MenuCard(title: String, onClick: () -> Unit) {
    Card (
        modifier = Modifier
            .size(140.dp)
            .clickable{ onClick()},
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ){
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ){

            Text(

                text = title,
                style = MaterialTheme.typography.titleMedium)

        }

    }
}
