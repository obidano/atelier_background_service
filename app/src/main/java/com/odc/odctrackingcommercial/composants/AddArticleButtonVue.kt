package com.odc.odctrackingcommercial.composants

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.odc.odctrackingcommercial.vues.IFormArticlesPage
import com.odc.odctrackingcommercial.ui.theme.*

@Composable
fun AddArticleButtonVue(
    ajouterArticle: () -> Unit,
    form: IFormArticlesPage?,
) {
    val open = remember {
        mutableStateOf(false)
    }
    TextButton(onClick = { open.value = true }) {
        Icon(Icons.Filled.Add, "", tint = MaterialTheme.colors.topAppBarContentColor)
        Spacer(modifier = Modifier.width(5.dp))
        Text("Créer", color = MaterialTheme.colors.topAppBarContentColor)
    }

    if (open.value)
        FormulaireArticleDialog(
            onClose = { open.value = false },
            ajouterArticle, form
        )

}

@Composable
fun FormulaireArticleDialog(
    onClose: () -> Unit = {},
    ajouterArticle: () -> Unit = {},
    form: IFormArticlesPage? = null,
) {


    val context = LocalContext.current


    Dialog(onDismissRequest = { }
    ) {
        Card() {
            Column(
                Modifier
                    .fillMaxWidth()
                    .scrollable(rememberScrollState(), Orientation.Vertical)
                    .padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text("Formulaire Article")
                Spacer(modifier = Modifier.height(20.dp))

                MTextField(
                    "Nom Article", form?.nom ?: "",
                    onChange = { form?.nom = it }, dialogFocus = true

                )
                Spacer(modifier = Modifier.height(20.dp))

                MTextField(
                    "Prix Vente", form?.prixVente ?: "",
                    onChange = { form?.prixVente = it },
                    kb = KeyboardType.Number
                )
                Spacer(modifier = Modifier.height(20.dp))

//                Divider(thickness = 2.dp)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp, bottom = 20.dp)
                ) {

                    Button(
                        onClick = { onClose() }, elevation = ButtonDefaults.elevation(0.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.cancelButtonBgColor)
                    ) {
                        Text("Annuler")
                    }
                    Spacer(modifier = Modifier.width(15.dp))

                    Button(colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.confirmButtonBgColor),
                        onClick = {
                            if ((form?.nom?.isEmpty() == true) ||
                                form?.prixVente?.isEmpty() == true
                                || form?.prixVente?.toDoubleOrNull() == null
                            ) {
                                Toast.makeText(
                                    context,
                                    "Certains champs sont obligatoires",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }
                            onClose()
                            ajouterArticle()
                        }) {
                        Text("Ajouter", color = MaterialTheme.colors.confirmButtonTextColor)
                    }
                }
            }

        }
    }


}

@Composable
@Preview(showBackground = true)
fun PreviewFormulaireArticleDialog() {
    FormulaireArticleDialog()
}


@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun DarkPreviewFormulaireArticleDialog() {
    FormulaireArticleDialog()
}





