from datetime import datetime

from airflow.models.dag import DAG
from airflow.operators.bash import BashOperator
from airflow.contrib.operators.ssh_operator import SSHOperator
import pendulum

dag = DAG(dag_id="example_dag_tag", schedule="0 * * * *",
          tags=["example"],
          start_date=pendulum.datetime(2021, 1, 1, tz="UTC"))
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