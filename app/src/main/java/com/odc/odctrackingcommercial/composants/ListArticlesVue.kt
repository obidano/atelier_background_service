@file:OptIn(ExperimentalMaterialApi::class)

package com.odc.odctrackingcommercial.composants

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.outlined.Shop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odc.odctrackingcommercial.lib.utils.Categorie
import com.odc.odctrackingcommercial.lib.utils.Constantes
import com.odc.odctrackingcommercial.models.ActivitesModel
import com.odc.odctrackingcommercial.models.ArticlesModel
import com.odc.odctrackingcommercial.ui.theme.*

@Composable
fun ListArticlesVue(listArticles: List<ArticlesModel>) {
    LazyColumn(verticalArrangement = Arrangement.Top) {
        items(listArticles.size, key = {
            listArticles[it].id
        }) {
            val data = listArticles[it]
            ArticleVueItem(data, it)
        }
    }
}

@Composable
@Preview
fun ArticleVueItem(
    data: ArticlesModel = Constantes.FakeArticles[0],
    index: Int = 0,
    naviguerVersDetail: (Int) -> Unit = {},
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = if (index % 2 == 0)  MaterialTheme.colors.rowBackgroundColor else MaterialTheme.colors.rowBackgroundColor2,
        shape = RectangleShape,
        elevation = 2.dp, onClick = {}
    ) {

        Column(
            Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Sell, "", tint = MaterialTheme.colors.rowIconColor)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    data.nom,
                    color = MaterialTheme.colors.rowTextColor,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Box(modifier=Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd){
                    Text(
                        "CDF ${data.prixVente.toString()}",
                        color = MaterialTheme.colors.rowTextColor,
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }
        }


    }

}
