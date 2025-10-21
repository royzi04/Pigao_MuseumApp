package ph.edu.comteq.thealpshotels

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/**
 * A reusable search bar.
 */
@Composable
fun HotelsSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search..."
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text(placeholder) },
        singleLine = true
    )
}

@Preview(showBackground = true)
@Composable
private fun HotelsSearchBarPreview() {
    HotelsSearchBar(
        query = "",
        onQueryChange = {},
    )
}
