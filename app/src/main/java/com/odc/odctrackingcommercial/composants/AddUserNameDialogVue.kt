package com.odc.odctrackingcommercial.composants

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.odc.odctrackingcommercial.models.UserModel
import com.odc.odctrackingcommercial.vue_models.SharedVueModel
import com.odc.odctrackingcommercial.ui.theme.*
import kotlinx.coroutines.delay
import java.util.*

interface IFormuserPage {
    var nom: String
}

interface IAddUserNameDialogVue {
    val saveUser: () -> Any
}

@Composable
fun AddUserNameDialogVue(
    shareVM: SharedVueModel,
    scaffoldState: ScaffoldState,
) {
    val context = LocalContext.current
    val snackbarHostState = scaffoldState.snackbarHostState
    val open = remember {
        mutableStateOf(false)
    }
    var isSuccess by remember {
        mutableStateOf(false)
    }
    var notifMsg by remember {
        mutableStateOf("")
    }

    LaunchedEffect(shareVM,open.value) {
        delay(1700L)
        open.value = shareVM.identifiant == null
    }

    val form = object : IFormuserPage {
        override var nom by remember {
            mutableStateOf("")
        }

    }

    val fn = object : IAddUserNameDialogVue {
        override val saveUser = {
            if (form.nom.isEmpty()) {
                Toast.makeText(
                    context,
                    "Certains champs sont obligatoires",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                isSuccess = true
                notifMsg = "Identifiant Enregistré"
                val newUSer = UserModel(UUID.randomUUID().toString(), form.nom)
                shareVM.saveUserState(newUSer)
            }
        }

    }

    ShowSnackBar(snackbarHostState, notifMsg, isSuccess) {
        notifMsg = ""
        if (isSuccess) {
            open.value = false
        }
        isSuccess = false

    }



    if (open.value)
        FormulaireUserDialog(
            onClose = { open.value = false },
            fn, form
        )

}

@Composable
fun FormulaireUserDialog(
    onClose: () -> Unit = {},
    fn: IAddUserNameDialogVue? = null,
    form: IFormuserPage? = null,
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
                Text("Enregistrer votre identifiant")
                Spacer(modifier = Modifier.height(20.dp))

                MTextField(
                    "Votre Nom", form?.nom,
                    onChange = { form?.nom = it }, dialogFocus = true

                )
                Spacer(modifier = Modifier.height(20.dp))

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
                            fn?.saveUser?.invoke()
                            onClose()

                        }) {
                        Text("Confirmer", color = MaterialTheme.colors.confirmButtonTextColor)
                    }
                }
            }

        }
    }


}

@Composable
@Preview(showBackground = true)
fun PreviewFormulaireUsereDialog() {
    FormulaireUserDialog()
}


@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun DarkPreviewFormulaireUserDialog() {
    FormulaireUserDialog()
}





