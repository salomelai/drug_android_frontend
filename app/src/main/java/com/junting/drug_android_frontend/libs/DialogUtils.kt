// DialogUtils.kt

import android.content.Context
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.junting.drug_android_frontend.R
import com.junting.drug_android_frontend.libs.listeners.OnEditListener

object DialogUtils {
    fun initTextViewEditDialog(context: Context, onclickLayout:View, tv: TextView, title: String, onlyDigitInput: Boolean, listener: OnEditListener) {
        onclickLayout.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(context)
            builder.setTitle(title)

            // 建立一個 EditText 供使用者輸入新的藥物名稱
            val input = EditText(context)
            input.setText(tv.text)

            if (onlyDigitInput) {
                input.inputType = InputType.TYPE_CLASS_NUMBER
            }
            builder.setView(input)

            // 設定確認和取消按鈕
            builder.setPositiveButton(context.getString(R.string.confirm)) { dialog, which ->
                listener.onEdit(input.text.toString())
            }
            builder.setNegativeButton(context.getString(R.string.cancel)) { dialog, which ->
                dialog.cancel()
            }

            // 顯示對話框
            builder.show()
        }
    }
}
