package com.example.nyoba

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(
    private val list: ArrayList<Transaction>,
    private val onDelete: (Transaction) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    class ViewHolder(view: View)
        : RecyclerView.ViewHolder(view) {

        val txtDescription =
            view.findViewById<TextView>(
                R.id.txtDescription
            )

        val txtAmount =
            view.findViewById<TextView>(
                R.id.txtAmount
            )

        val txtDate =
            view.findViewById<TextView>(
                R.id.txtDate
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_transaction,
                    parent,
                    false
                )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val transaction =
            list[position]

        holder.txtDescription.text =
            transaction.description

        holder.txtAmount.text =
            "Rp %.0f".format(
                transaction.amount
            )

        holder.txtDate.text =
            transaction.date

        // ==========================
        // DETAIL TRANSACTION
        // ==========================

        holder.itemView.setOnClickListener {

            AlertDialog.Builder(
                holder.itemView.context
            )
                .setTitle(
                    "Transaction Detail"
                )
                .setMessage(
                    """
Type:
${transaction.type}

Amount:
Rp ${"%.0f".format(transaction.amount)}

Category:
${transaction.category}

Description:
${transaction.description}

Date:
${transaction.date}
                    """.trimIndent()
                )

                .setPositiveButton(
                    "OK",
                    null
                )

                .setNeutralButton(
                    "Delete"
                ) { _, _ ->

                    AlertDialog.Builder(
                        holder.itemView.context
                    )
                        .setTitle(
                            "Delete Transaction"
                        )
                        .setMessage(
                            "Apakah Anda yakin ingin menghapus transaksi ini?"
                        )
                        .setPositiveButton(
                            "Hapus"
                        ) { _, _ ->

                            onDelete(transaction)

                        }
                        .setNegativeButton(
                            "Batal",
                            null
                        )
                        .show()
                }

                .show()
        }
    }
}