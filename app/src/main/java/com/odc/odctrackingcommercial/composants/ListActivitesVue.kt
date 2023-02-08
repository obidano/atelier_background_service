@file:OptIn(ExperimentalMaterialApi::class)

package com.odc.odctrackingcommercial.composants

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
import com.odc.odctrackingcommercial.vues.IActivitesPage
import com.odc.odctrackingcommercial.ui.theme.*

@Composable
fun ListActivitesVue(listActivites: List<ActivitesModel>, fn: IActivitesPage?=null) {
    LazyColumn(verticalArrangement = Arrangement.Top) {
        items(listActivites.size, key = {
            listActivites[it].id
        }) {
            val data = listActivites[it]
            ActiviteVueItem(data, it, fn)
        }
    }
}

@Composable
@Preview
fun ActiviteVueItem(
    data: ActivitesModel = Constantes.FakeActivites[0],
    index: Int = 0,
    fn: IActivitesPage?=null,

    ) {

    val contentStyle = @Composable { title: String, value: String ->
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colors.rowTextColor,
                    fontSize = MaterialTheme.typography.caption.fontSize,
                    fontWeight = FontWeight.Light,
                )
            ) {
                append("$title: ")
            }

            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colors.rowTextColor,
                    fontSize = MaterialTheme.typography.caption.fontSize,
                    fontWeight = FontWeight.Normal,
                )
            ) {
                append(value)
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = if (index % 2 == 0) MaterialTheme.colors.rowBackgroundColor else MaterialTheme.colors.rowBackgroundColor2,
        shape = RectangleShape,
        elevation = 4.dp, onClick = {
            fn?.naviguerVersDetail?.invoke(data.id)
        }
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 10.dp)
        ) {
            Icon(Icons.Outlined.Shop, "", tint = MaterialTheme.colors.rowIconColor)
            Column(
                Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "${data.category.display}:",
                        color = MaterialTheme.colors.rowTextColor,
                        style = MaterialTheme.typography.caption,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        data.article.nom,
                        color = MaterialTheme.colors.rowTextColor,
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Canvas(
                            modifier = Modifier
                                .size(6.dp)
                        ) {
                            drawCircle(color = data.category.color)
                        }
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val prix =
                        if (data.category == Categorie.Revenus) data.article.prixVente else (data.prixUnitAchat as Double)
                    Text(contentStyle("Qte", data.quantite.toString()))
                    Text(
                        "|",
                        color = LightGray,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                    )
                    Text(contentStyle("PU", prix.toString()))

                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "CDF ${prix * data.quantite}",
                            color = MaterialTheme.colors.rowTextColor,
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }
            }
        }


    }

}
