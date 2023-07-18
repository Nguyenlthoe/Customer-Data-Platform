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

dag = DAG(dag_id="update_information", schedule="1 0 * * *",
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

update_short_hobbies_bash = init_env_bash +  """
cd / &&
ls &&
bash update_short_hobbies.sh  
"""
update_short_hobbies = SSHOperator(
    conn_timeout = 2000,
    ssh_conn_id='hadoop-master',
    task_id='run_update_short_hobbies',
    command=update_short_hobbies_bash,
    dag=dag)

update_long_hobbies_bash = init_env_bash + """
cd / &&
ls &&
bash update_long_hobbies.sh   
"""
update_long_hobbies = SSHOperator(
    conn_timeout = 2000,
    ssh_conn_id='hadoop-master',
    task_id='run_update_long_hobbies',
    command=update_long_hobbies_bash,
    dag=dag)
[update_short_hobbies, update_long_hobbies]