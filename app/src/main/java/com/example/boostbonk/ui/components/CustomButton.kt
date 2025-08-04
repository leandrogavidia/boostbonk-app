import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boostbonk.ui.theme.BonkGray
import com.example.boostbonk.ui.theme.BonkOrange
import com.example.boostbonk.ui.theme.BonkWhite
import com.example.boostbonk.ui.theme.BoostBonkTheme

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    backgroundColor: Color,
    contentColor: Color = BonkWhite,
    isLoading: Boolean = false,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.height(48.dp),
        enabled = !isLoading && enabled,
    ) {
        if (isLoading) {
            androidx.compose.material3.CircularProgressIndicator(
                color = BonkWhite,
                strokeWidth = 2.dp,
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
            )
        } else {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                letterSpacing = 4.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun ReceiveCustomButtonPreview() {
    BoostBonkTheme {
            CustomButton(
                onClick = {},
                icon = Icons.Filled.ArrowDownward,
                text = "Receive",
                backgroundColor = BonkOrange,
            )
    }
}

@Preview(showBackground = true)
@Composable
fun SendCustomButtonPreview() {
    BoostBonkTheme {
        CustomButton(
            onClick = {},
            icon = Icons.Filled.ArrowOutward,
            text = "Withdraw",
            backgroundColor = BonkGray,
        )
    }
}
