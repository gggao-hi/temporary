import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.sql.DriverManager

object Logger {
    private var writer: PrintWriter? = null
    fun init() {
        writer = PrintWriter(FileWriter(File("log.txt").apply { this.createNewFile() }), true)
        DriverManager.setLogWriter(writer)
    }

    fun write(msg: String) {
        writer?.write(msg)
        writer?.write("\n")
        writer?.flush()
    }

    fun close() {
        writer?.close()
    }

}