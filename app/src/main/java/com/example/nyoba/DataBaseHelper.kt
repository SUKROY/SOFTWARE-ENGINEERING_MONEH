package com.example.nyoba

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(
        context,
        "UserDatabase",
        null,
        4
    ){

    override fun onCreate(db: SQLiteDatabase) {

        val createUserTable = """
            CREATE TABLE users(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nama TEXT,
                email TEXT,
                password TEXT
            )
        """.trimIndent()

        val createTransactionTable = """
            CREATE TABLE transactions(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                type TEXT,
                amount REAL,
                category TEXT,
                date TEXT,
                description TEXT
            )
        """.trimIndent()

        db.execSQL(createUserTable)

        val createBudgetTable = """
        CREATE TABLE budgets(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            amount REAL
        )
        """.trimIndent()

        db.execSQL(createBudgetTable)

        db.execSQL(createTransactionTable)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS transactions")
        db.execSQL("DROP TABLE IF EXISTS budgets")
        onCreate(db)
    }

    // =================================
    // USER
    // =================================

    fun insertUser(
        nama: String,
        email: String,
        password: String
    ): Boolean {

        val db = writableDatabase

        val values = ContentValues()

        values.put("nama", nama)
        values.put("email", email)
        values.put("password", password)

        val result =
            db.insert(
                "users",
                null,
                values
            )

        db.close()

        return result != -1L
    }

    fun checkUser(
        email: String,
        password: String
    ): Boolean {

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE email=? AND password=?",
            arrayOf(email, password)
        )

        val exists = cursor.count > 0

        cursor.close()
        db.close()

        return exists
    }

    fun isEmailExists(
        email: String
    ): Boolean {

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE email=?",
            arrayOf(email)
        )

        val exists = cursor.count > 0

        cursor.close()
        db.close()

        return exists
    }

    fun getUserName(
        email: String
    ): String {

        val db =
            readableDatabase

        val cursor =
            db.rawQuery(
                """
            SELECT nama
            FROM users
            WHERE email=?
            """.trimIndent(),
                arrayOf(email)
            )

        var username = ""

        if(cursor.moveToFirst()) {

            username =
                cursor.getString(0)
        }

        cursor.close()
        db.close()

        return username
    }

    // =================================
    // TRANSACTION
    // =================================

    fun insertTransaction(
        type: String,
        amount: Double,
        category: String,
        date: String,
        description: String
    ): Boolean {

        val db = writableDatabase

        val values = ContentValues()

        values.put("type", type)
        values.put("amount", amount)
        values.put("category", category)
        values.put("date", date)
        values.put("description", description)

        val result = db.insert(
            "transactions",
            null,
            values
        )

        db.close()

        return result != -1L
    }

    fun getAllTransactions(): Cursor {

        val db = readableDatabase

        return db.rawQuery(
            """
            SELECT *
            FROM transactions
            ORDER BY id DESC
            """.trimIndent(),
            null
        )
    }

    // =================================
    // CATEGORY AUTOCOMPLETE
    // =================================

    fun getAllCategories(): ArrayList<String> {

        val categoryList = ArrayList<String>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT DISTINCT category
            FROM transactions
            ORDER BY category ASC
            """.trimIndent(),
            null
        )

        while (cursor.moveToNext()) {

            categoryList.add(
                cursor.getString(0)
            )
        }

        cursor.close()
        db.close()

        return categoryList
    }

    // =================================
    // DASHBOARD SUMMARY
    // =================================

    fun getTotalIncome(): Double {

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT SUM(amount)
            FROM transactions
            WHERE type='Income'
            """.trimIndent(),
            null
        )

        var total = 0.0

        if (cursor.moveToFirst()) {

            if (!cursor.isNull(0)) {
                total = cursor.getDouble(0)
            }
        }

        cursor.close()
        db.close()

        return total
    }

    fun getTotalExpense(): Double {

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT SUM(amount)
            FROM transactions
            WHERE type='Expense'
            """.trimIndent(),
            null
        )

        var total = 0.0

        if (cursor.moveToFirst()) {

            if (!cursor.isNull(0)) {
                total = cursor.getDouble(0)
            }
        }

        cursor.close()
        db.close()

        return total
    }

    fun getBalance(): Double {

        return getTotalIncome() - getTotalExpense()
    }

    fun getTransactionList():
            ArrayList<Transaction> {

        val list =
            ArrayList<Transaction>()

        val db =
            readableDatabase

        val cursor =
            db.rawQuery(
                "SELECT * FROM transactions ORDER BY id DESC",
                null
            )

        while(cursor.moveToNext()) {

            list.add(
                Transaction(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
                )
            )
        }

        cursor.close()
        db.close()

        return list
    }

    fun deleteTransaction(
        id: Int
    ): Boolean {

        val db =
            writableDatabase

        val result =
            db.delete(
                "transactions",
                "id=?",
                arrayOf(id.toString())
            )

        db.close()

        return result > 0
    }

    fun getExpenseByCategory(): HashMap<String, Double> {

        val result =
            HashMap<String, Double>()

        val db =
            readableDatabase

        val cursor =
            db.rawQuery(
                """
            SELECT category,
                   SUM(amount)
            FROM transactions
            WHERE type='Expense'
            GROUP BY category
            """,
                null
            )

        while(cursor.moveToNext()) {

            val category =
                cursor.getString(0)

            val amount =
                cursor.getDouble(1)

            result[category] = amount
        }

        cursor.close()

        return result
    }
    fun getMonthlyExpense(): ArrayList<MonthlyExpense> {

        val result =
            ArrayList<MonthlyExpense>()

        val monthMap =
            HashMap<String, Double>()

        val db =
            readableDatabase

        val cursor =
            db.rawQuery(
                """
            SELECT date, amount
            FROM transactions
            WHERE type='Expense'
            """,
                null
            )

        while(cursor.moveToNext()) {

            val date =
                cursor.getString(0)

            val amount =
                cursor.getDouble(1)

            try {

                val month =
                    date.substring(3, 6)

                monthMap[month] =
                    (monthMap[month] ?: 0.0) +
                            amount

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }

        cursor.close()

        val months =
            arrayOf(
                "Jan","Feb","Mar","Apr",
                "May","Jun","Jul","Aug",
                "Sep","Oct","Nov","Dec"
            )

        for(month in months) {

            result.add(
                MonthlyExpense(
                    month,
                    monthMap[month] ?: 0.0
                )
            )
        }

        return result

    }
    fun getMonthlyFinance(): ArrayList<MonthlyFinance> {

        val result =
            ArrayList<MonthlyFinance>()

        val incomeMap =
            HashMap<String, Double>()

        val expenseMap =
            HashMap<String, Double>()

        val db =
            readableDatabase

        val cursor =
            db.rawQuery(
                """
            SELECT type, amount, date
            FROM transactions
            """,
                null
            )

        while(cursor.moveToNext()) {

            val type =
                cursor.getString(0)

            val amount =
                cursor.getDouble(1)

            val date =
                cursor.getString(2)

            try {

                val month =
                    date.substring(3, 6)

                if(type == "Income") {

                    incomeMap[month] =
                        (incomeMap[month] ?: 0.0) +
                                amount

                } else {

                    expenseMap[month] =
                        (expenseMap[month] ?: 0.0) +
                                amount
                }

            } catch(_: Exception) {

            }
        }

        cursor.close()

        val months =
            arrayOf(
                "Jan",
                "Feb",
                "Mar",
                "Apr",
                "May",
                "Jun",
                "Jul",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec"
            )

        for(month in months) {

            result.add(
                MonthlyFinance(
                    month,
                    incomeMap[month] ?: 0.0,
                    expenseMap[month] ?: 0.0
                )
            )
        }

        return result
    }

    fun getMonthlyBalance(): ArrayList<MonthlyBalance> {

        val result =
            ArrayList<MonthlyBalance>()

        val incomeMap =
            HashMap<String, Double>()

        val expenseMap =
            HashMap<String, Double>()

        val db =
            readableDatabase

        val cursor =
            db.rawQuery(
                """
            SELECT type, amount, date
            FROM transactions
            """,
                null
            )

        while(cursor.moveToNext()) {

            val type =
                cursor.getString(0)

            val amount =
                cursor.getDouble(1)

            val date =
                cursor.getString(2)

            try {

                val month =
                    date.substring(3, 6)

                if(type == "Income") {

                    incomeMap[month] =
                        (incomeMap[month] ?: 0.0) +
                                amount

                } else {

                    expenseMap[month] =
                        (expenseMap[month] ?: 0.0) +
                                amount
                }

            } catch(_: Exception) {

            }
        }

        cursor.close()

        val months =
            arrayOf(
                "Jan","Feb","Mar","Apr",
                "May","Jun","Jul","Aug",
                "Sep","Oct","Nov","Dec"
            )

        for(month in months) {

            val income =
                incomeMap[month] ?: 0.0

            val expense =
                expenseMap[month] ?: 0.0

            val balance =
                income - expense

            // hanya bulan yang memiliki transaksi
            if(
                income != 0.0 ||
                expense != 0.0
            ) {

                result.add(
                    MonthlyBalance(
                        month,
                        balance
                    )
                )
            }
        }

        return result
    }
    fun saveBudget(
        amount: Double
    ): Boolean {

        val db =
            writableDatabase

        db.delete(
            "budgets",
            null,
            null
        )

        val values =
            ContentValues()

        values.put(
            "amount",
            amount
        )

        val result =
            db.insert(
                "budgets",
                null,
                values
            )

        db.close()

        return result != -1L
    }

    fun getBudget(): Double {

        val db =
            readableDatabase

        val cursor =
            db.rawQuery(
                """
            SELECT amount
            FROM budgets
            LIMIT 1
            """,
                null
            )

        var budget =
            0.0

        if(cursor.moveToFirst()) {

            budget =
                cursor.getDouble(0)
        }

        cursor.close()
        db.close()

        return budget
    }
}