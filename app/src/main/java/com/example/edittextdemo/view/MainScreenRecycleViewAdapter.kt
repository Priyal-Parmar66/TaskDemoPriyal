import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.edittextdemo.databinding.ItemDynamicEditTextBinding

class EditTextAdapter(
    private val itemList: MutableList<String>,
    private val onRemove: (Int) -> Unit
) : RecyclerView.Adapter<EditTextAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemDynamicEditTextBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val editText: EditText = binding.editText
        val removeButton: ImageButton = binding.removeButton
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDynamicEditTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.editText.setText(itemList[position])

        //Add new TextWatcher safely
        holder.editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (holder.bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    itemList[holder.bindingAdapterPosition] = s.toString()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        holder.removeButton.setOnClickListener { onRemove(holder.adapterPosition) }

        holder.editText.error = errorMap[position]

//        val text = itemList[position].trim()
//        when {
//            text.isEmpty() -> holder.editText.error = "Field cannot be empty"
//            text.length < 6 || text.length > 11 -> holder.editText.error =
//                "Text must be between 6 and 11 characters"
//            else -> holder.editText.error = null // Clear error if valid
//        }


        // Set IME action
        holder.editText.imeOptions = if (position == itemList.size - 1) {
            EditorInfo.IME_ACTION_DONE
        } else {
            EditorInfo.IME_ACTION_NEXT
        }
    }

    private val errorMap = mutableMapOf<Int, String?>()

    fun validateFields(): Boolean {
        var isValid = true
        errorMap.clear()
        for (i in itemList.indices) {
            val text = itemList[i].trim()
            when {
                text.isEmpty() -> {
                    errorMap[i] = "Field cannot be empty"
                    isValid = false
                }

                text.length < 6 || text.length > 11 -> {
                    errorMap[i] = "Text must be between 6 and 11 characters"
                    isValid = false
                }

                else -> {
                    errorMap[i] = null
                }
            }
        }
        notifyDataSetChanged()
        return isValid
    }


    private var recyclerView: RecyclerView? = null


    private fun findEditText(position: Int): EditText? {
        return (recyclerView?.findViewHolderForAdapterPosition(position) as? ViewHolder)?.binding?.editText
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val movedItem = itemList.removeAt(fromPosition)
        itemList.add(toPosition, movedItem)
        notifyItemMoved(fromPosition, toPosition)
        notifyItemChanged(fromPosition)
        notifyItemChanged(toPosition)
    }

    override fun getItemCount(): Int = itemList.size
}