import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application


fun main() = application {
    Window(
        icon = painterResource("app_static_widget_map.png"),
        title = "Feature Flag Config",
        onCloseRequest = ::exitApplication
    ) {
        HomePage()
    }
}
