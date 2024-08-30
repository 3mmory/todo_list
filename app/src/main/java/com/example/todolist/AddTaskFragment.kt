import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.todolist.R

class AddTaskFragment : DialogFragment() {

    private lateinit var onTaskAdded: (String, String) -> Unit

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext()).apply {
            setTitle(arguments?.getString("title", "Add Task"))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_add_task, container, false)

        val editTextTask = view.findViewById<EditText>(R.id.editTextTask)
        val editTextDescription = view.findViewById<EditText>(R.id.editTextDescription)
        val buttonSave = view.findViewById<Button>(R.id.buttonSave)

        // تعبئة الحقول إذا كان هناك بيانات قادمة من عملية التعديل
        arguments?.let {
            editTextTask.setText(it.getString("task"))
            editTextDescription.setText(it.getString("description"))
        }

        buttonSave.setOnClickListener {
            val task = editTextTask.text.toString()
            val description = editTextDescription.text.toString()
            if (task.isNotEmpty()) {
                onTaskAdded(task, description)
                dismiss()
            }
        }

        return view
    }

    fun setOnTaskAddedListener(listener: (String, String) -> Unit) {
        onTaskAdded = listener
    }

    companion object {
        // دالة لإنشاء مثال جديد من AddTaskFragment مع البيانات القديمة
        fun newInstance(task: String, description: String): AddTaskFragment {
            val fragment = AddTaskFragment()
            val args = Bundle().apply {
                putString("task", task)
                putString("description", description)
                putString("title", "Edit Task")
            }
            fragment.arguments = args
            return fragment
        }
    }
}
