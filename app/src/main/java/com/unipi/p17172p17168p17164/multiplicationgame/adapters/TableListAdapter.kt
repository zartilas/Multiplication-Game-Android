package com.unipi.p17172p17168p17164.multiplicationgame.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.unipi.p17172p17168p17164.multiplicationgame.R
import com.unipi.p17172p17168p17164.multiplicationgame.databinding.ItemTableBinding
import com.unipi.p17172p17168p17164.multiplicationgame.models.MultiplicationTable
import com.unipi.p17172p17168p17164.multiplicationgame.ui.activities.TableResultActivity
import com.unipi.p17172p17168p17164.multiplicationgame.ui.activities.TablesListActivity
import com.unipi.p17172p17168p17164.multiplicationgame.utils.Constants


/**
 * A adapter class for tables list items.
 */
open class TablesListAdapter(
    private val tablesListActivity: TablesListActivity,
    private var list: ArrayList<MultiplicationTable>
) : RecyclerView.Adapter<TablesListAdapter.TablesViewHolder>() {

    /**
     * Inflates the item views which is designed in xml layout file
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TablesViewHolder {
        return TablesViewHolder(
            ItemTableBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     */
    override fun onBindViewHolder(holder: TablesViewHolder, position: Int) {
        val model = list[position]

        holder.binding.apply {
            txtViewHeader.text = model.name
            txtViewDescription.text = model.desc
            txtViewNumberIcon.text = model.number.toString()
        }
        holder.itemView.setOnClickListener {
            tablesListActivity.playButtonPressSound(tablesListActivity)
            val intent = Intent(tablesListActivity, TableResultActivity::class.java)
            intent.putExtra(Constants.EXTRA_NUMBER_FIRST, model.number)
            intent.putExtra(Constants.EXTRA_NUMBER_SECOND, 1)
            intent.putExtra(Constants.EXTRA_CORRECT_ANSWERS, 0)
            intent.putExtra(Constants.EXTRA_WRONG_ANSWERS, 0)
            intent.putExtra(Constants.EXTRA_LIMIT, model.limit)
            tablesListActivity.startActivity(intent)
        }

        // Slide from right animation
        val animation: Animation =
            AnimationUtils.loadAnimation(tablesListActivity, R.anim.anim_from_right)
        holder.itemView.startAnimation(animation)
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class TablesViewHolder(val binding: ItemTableBinding) : RecyclerView.ViewHolder(binding.root)
}