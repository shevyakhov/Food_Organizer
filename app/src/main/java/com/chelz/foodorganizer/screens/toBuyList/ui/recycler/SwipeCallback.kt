package com.chelz.foodorganizer.screens.toBuyList.ui.recycler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chelz.foodorganizer.R

open class SwipeCallback(
	private val context: Context,
	private val deleteCallback: (Int) -> Unit,
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

	private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
	private val deleteBackgroundColor = ContextCompat.getColor(context, R.color.light_error)
	private var swipedViewHolder: RecyclerView.ViewHolder? = null
	private var originalDx: Float = 0f
	private val deleteIconColor = Color.WHITE
	private val iconMargin = 80
	private val paint = Paint()
	private val deleteIcon = R.drawable.ic_delete
	private val intrinsicWidth = ContextCompat.getDrawable(context, deleteIcon)?.intrinsicWidth ?: 0
	private val intrinsicHeight = ContextCompat.getDrawable(context, deleteIcon)?.intrinsicHeight ?: 0

	override fun onMove(
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder,
		target: RecyclerView.ViewHolder,
	): Boolean {
		return false
	}

	override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
		swipedViewHolder = viewHolder
		originalDx = viewHolder.itemView.translationX
		val position = viewHolder.adapterPosition
		when (direction) {
			ItemTouchHelper.LEFT -> deleteCallback(position)
		}
	}

	override fun onChildDraw(
		c: Canvas,
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder,
		dX: Float,
		dY: Float,
		actionState: Int,
		isCurrentlyActive: Boolean,
	) {

		val itemView = viewHolder.itemView
		val itemHeight = itemView.bottom - itemView.top
		val isCanceled = dX == 0f && !isCurrentlyActive

		if (isCanceled) {
			clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
			super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
			return
		}
		// Restrict the swipe distance
		when {
			dX < 0 -> {
				paint.color = deleteBackgroundColor
				val background = android.graphics.RectF(
					itemView.right.toFloat() + dX,
					itemView.top.toFloat(),
					itemView.right.toFloat(),
					itemView.bottom.toFloat()
				)
				c.drawRect(background, paint)

				drawIcon(
					c,
					deleteIcon,
					deleteIconColor,
					itemView.right - iconMargin - intrinsicWidth,
					itemView.top + (itemHeight - intrinsicHeight) / 2,
					itemView.right - iconMargin,
					itemView.top + (itemHeight + intrinsicHeight) / 2
				)
			}
		}
		super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

	}

	private fun drawIcon(
		canvas: Canvas,
		iconRes: Int,
		color: Int,
		left: Int,
		top: Int,
		right: Int,
		bottom: Int,
	) {
		val icon = androidx.appcompat.content.res.AppCompatResources.getDrawable(
			context,
			iconRes
		)
		icon?.setBounds(left, top, right, bottom)
		icon?.setTint(color)
		icon?.draw(canvas)
	}

	private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
		c?.drawRect(left, top, right, bottom, clearPaint)
	}
}