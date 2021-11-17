package xyz.ridsoft.harumap

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.AnimationDrawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import xyz.ridsoft.harumap.databinding.RowRoutineBinding
import java.util.*

class RoutineViewHolder(private val binding: RowRoutineBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        const val VIEW_TYPE = R.layout.row_routine
    }

    var onLongClickListener: ((routine: Routine) -> Unit)? = null
    var onStateChangedListener: ((id: Int, done: Boolean) -> Unit)? = null

    fun bind(context: Context, routine: Routine) {

        val calendar = Calendar.getInstance()
        val task =
            DBHelper(context).getTask(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.DAY_OF_YEAR)
            )
        val done = task.routines[routine.id] == true

        binding.txtRowRoutine.text = routine.content
        binding.imgRowRoutine.visibility = if (done) View.VISIBLE else View.INVISIBLE

        // On done state changed
        binding.layoutRowRoutine.setOnClickListener {
            // Change icon visibility
            if (done) {
                binding.imgRowRoutine.visibility = View.INVISIBLE
            } else {
                binding.imgRowRoutine.visibility = View.VISIBLE
                val avd = binding.imgRowRoutine.drawable as AnimatedVectorDrawable
                avd.start()
            }

            // Toggle task
            onStateChangedListener?.let { it(routine.id, !done) }
        }

        // On long click
        binding.layoutRowRoutine.setOnLongClickListener {
            onLongClickListener?.let { it(routine) }
            true
        }
    }
}