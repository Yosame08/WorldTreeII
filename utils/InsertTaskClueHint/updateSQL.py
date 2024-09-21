import os
import mysql.connector
from mysql.connector import errorcode

def read_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        return file.read().strip()

def read_info_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        lines = file.readlines()
        info_data = [line.strip() for line in lines]
        # Convert the submission field to a boolean
        info_data[-1] = info_data[-1].lower() == 'true'
        return info_data

def connect_db(host, port, user, password, database):
    try:
        conn = mysql.connector.connect(
            host=host,
            port=port,
            user=user,
            password=password,
            database=database
        )
        return conn
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print("Something is wrong with your user name or password")
        elif err.errno == errorcode.ER_BAD_DB_ERROR:
            print("Database does not exist")
        else:
            print(err)
        return None

def get_task_data(task_id, cursor, task_table):
    cursor.execute(f"SELECT * FROM {task_table} WHERE task_id = %s", (task_id,))
    result = cursor.fetchone()
    if result:
        # Convert the submission field from integer to boolean
        result = list(result)
        result[8] = bool(result[8])
    return result

def get_hint_clue_data(task_id, cursor, hint_clue_table):
    cursor.execute(f"SELECT * FROM {hint_clue_table} WHERE task_id = %s", (task_id,))
    return cursor.fetchone()

def insert_task(data, cursor, task_table):
    cursor.execute(f"""
        INSERT INTO {task_table} (task_id, task_title, task_pos_x, task_pos_y, uri, task_point, task_coin, hint_price, submission, task_description, task_description_full)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
    """, data)

def insert_hint_clue(data, cursor, hint_clue_table):
    cursor.execute(f"""
        INSERT INTO {hint_clue_table} (task_id, hint, clue)
        VALUES (%s, %s, %s)
    """, data)

def update_task(data, cursor, task_table):
    cursor.execute(f"""
        UPDATE {task_table}
        SET task_title = %s, task_pos_x = %s, task_pos_y = %s, uri = %s, task_point = %s, task_coin = %s, hint_price = %s, submission = %s, task_description = %s, task_description_full = %s
        WHERE task_id = %s
    """, data[1:] + (data[0],))

def update_hint_clue(data, cursor, hint_clue_table):
    cursor.execute(f"""
        UPDATE {hint_clue_table}
        SET hint = %s, clue = %s
        WHERE task_id = %s
    """, data[1:] + (data[0],))

def main():
    host = 'localhost'
    port = 3306 # input("Enter MySQL port: ")
    user = 'root' # input("Enter MySQL username: ")
    password = input("Enter MySQL password: ")
    database = input("Enter database name: ")
    conn = connect_db(host, port, user, password, database)
    if conn is None:
        return
    print("Connected to database")

    task_table = 'task' # input("Enter task table name: ")
    hint_clue_table = 'hint_clue' # input("Enter hint_clue table name: ")

    base_dir = './'
    cursor = conn.cursor()

    for filename in os.listdir(os.path.join(base_dir, 'Info')):
        if filename.endswith('.txt'):
            task_id = filename[:-4]

            info_data = read_info_file(os.path.join(base_dir, 'Info', filename))
            task_title, task_pos_x, task_pos_y, uri, task_point, task_coin, hint_price, submission = info_data

            task_description = read_file(os.path.join(base_dir, 'Story', 'base', filename))
            task_description_full = task_description + read_file(os.path.join(base_dir, 'Story', 'pass', filename))

            hint = read_file(os.path.join(base_dir, 'Hint', filename))
            clue = read_file(os.path.join(base_dir, 'Clue', filename))

            task_data = (task_id, task_title, task_pos_x, task_pos_y, uri, task_point, task_coin, hint_price, submission, task_description, task_description_full)
            hint_clue_data = (task_id, hint, clue)

            existing_task = get_task_data(task_id, cursor, task_table)
            if existing_task:
                if existing_task != task_data:
                    user_input = input(f"Task {task_id} data is inconsistent. Do you want to update it? (yes/no): ")
                    if user_input.lower() == 'yes':
                        update_task(task_data, cursor, task_table)
                        print(f"Updated task {task_id}")
            else:
                insert_task(task_data, cursor, task_table)
                print(f"Inserted new task {task_id}")

            existing_hint_clue = get_hint_clue_data(task_id, cursor, hint_clue_table)
            if existing_hint_clue:
                if existing_hint_clue != hint_clue_data:
                    user_input = input(f"Hint/Clue for task {task_id} is inconsistent. Do you want to update it? (yes/no): ")
                    if user_input.lower() == 'yes':
                        update_hint_clue(hint_clue_data, cursor, hint_clue_table)
                        print(f"Updated hint/clue for task {task_id}")
            else:
                insert_hint_clue(hint_clue_data, cursor, hint_clue_table)
                print(f"Inserted new hint/clue for task {task_id}")

    conn.commit()
    cursor.close()
    conn.close()

if __name__ == "__main__":
    main()