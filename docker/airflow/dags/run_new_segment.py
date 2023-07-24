from datetime import datetime

from airflow.models.dag import DAG
from airflow.providers.ssh.operators.ssh import SSHOperator
import pendulum

DEFAULT_ARGS = {
    'owner': 'airflow',
    'depends_on_past': False,
    'email_on_failure': False,
    'email_on_retry': False,
    'retries': 0
}

dag = DAG(dag_id="run_new_segment", schedule="1 * * * *",
          tags=["cdp"],
          start_date=pendulum.datetime(2021, 1, 1, tz="UTC"),
          max_active_runs=1,
          default_args=DEFAULT_ARGS)
# Step 1 - init env
init_env_bash = """
export SPARK_HOME=/usr/local/spark-3.1.2-bin-hadoop3.2 &&
export PATH=$PATH:$SPARK_HOME/bin &&
export HADOOP_HOME=/usr/local/hadoop &&
export HADOOP_CONF_DIR="$HADOOP_HOME/etc/hadoop" &&
export YARN_CONF_DIR="$HADOOP_HOME/etc/hadoop" &&

"""

run_new_segment_bash = init_env_bash + """
cd / &&
ls &&
bash run_new_segment.sh   
"""
run_new_segment = SSHOperator(
    cmd_timeout = 2000,
    conn_timeout = 2000,
    ssh_conn_id='hadoop-master',
    task_id='run_new_segment',
    command=run_new_segment_bash,
    dag=dag)
[run_new_segment]