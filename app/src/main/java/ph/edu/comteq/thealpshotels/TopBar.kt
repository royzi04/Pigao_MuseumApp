package ph.edu.comteq.thealpshotels

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A reusable top bar for hotel screens.
 *
 * - Displays a title on the left.
 * - Optionally shows a logo image next to the title.
 * - Accepts a trailing slot for actions (e.g., profile icon, menu).
 */
@Composable
fun HotelsTopBar(
    title: String,
    modifier: Modifier = Modifier,
    logoPainter: Painter? = null,
    trailingContent: @Composable (RowScope.() -> Unit) = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                modifier = Modifier.padding(end = 8.dp),
                style = MaterialTheme.typography.titleMedium
            )
            if (logoPainter != null) {
                Image(
                    painter = logoPainter,
                    contentDescription = null,
                    modifier = Modifier.width(40.dp)
                )
            }
        }
        Row(content = trailingContent)
    }
}

@Preview(showBackground = true)
@Composable
private fun HotelsTopBarPreview() {
    HotelsTopBar(
        title = "The Alps Hotel"
    )
}
