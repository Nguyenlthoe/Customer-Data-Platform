from datetime import timedelta, datetime
import airflow
from airflow import DAG
from airflow.contrib.operators.ssh_operator import SSHOperator

default_args = {
    'owner': 'airflow',
    'depends_on_past': False,
    'email': ['airflow@example.com'],
    'email_on_failure': False,
    'email_on_retry': False,
    'start_date': datetime.now() - timedelta(minutes=20),
    'retries': 1,
    'retry_delay': timedelta(minutes=5),
}
dag = DAG(dag_id='testing_stuff',
          default_args=default_args,
          schedule_interval='0 * * * *',
          dagrun_timeout=timedelta(seconds=120))
# Step 1 - Dump data from postgres databases
t1_bash = """
echo 'Hello World'
"""
t1 = SSHOperator(
    ssh_conn_id='hadoop-master',
    task_id='test_ssh_operator',
    command=t1_bash,
    dag=dag)
t1