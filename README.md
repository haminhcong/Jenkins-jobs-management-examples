# Test use jenkins-job-wrecker and jenkins-job-builder to export, create and manage jenkins jobs


## 1. References

- <https://github.com/ktdreyer/jenkins-job-wrecker>
- <https://github.com/openstack-infra/jenkins-job-builder>
- <https://docs.openstack.org/infra/jenkins-job-builder/>

## 2. Prerequisites
- A jenkins server and jobs defined before (example: <http://192.168.122.10:8080> )
- Jenkins user_name and password (example **user_name**: USER, **password**: USER_PASS)

## 3. How to do?
- Create a working directory, example ```/home/user/working_dir``` and jump to it
- Install python virtual environment ```virtualenv venv```
- Use it: ```. venv/bin/active```
- Install jenkins-job-wreker and jenkins-job-builder follows fererenced documents
- Generate job_config files of current jobs in jenkins server in YAML format by jenkins-job-wrecker:

```bash

cd exported_jobs

export JJW_USERNAME=USER
export JJW_PASSWORD=USER_PASS

jjwrecker -s http://192.168.122.10:8080

```

YAML job config files will be exported and saved in **output** folder.

- Copy yaml job config file to managed_jobs folder, make changes then test it:

```bash
cd managed_jobs
jenkins-jobs test JavaWS_Management.yml
```

Output jenkins XML config file will be saved in managed_jobs/output folder

- Check if output satisfies you needs. If it is ok, upload our new job to jenkins server:
  - Create jenkins server config file jenkins_job.ini for jenkins server
  - Use this authentication config file to upload YAML to Jenkins server to create/update job in jenkins server.

```bash
vim jenkins_job.ini
// follow sample jenkins_jobs.ini file

[job_builder]
ignore_cache=True
keep_descriptions=False
include_path=.:scripts:~/git/
recursive=False
exclude=.*:manual:./development
allow_duplicates=False

[jenkins]
user=JENKINS_USER_NAME
password=JENKINS_USER_PASSWORD
url=http://192.168.122.10:8080
#query_plugins_info=False
##### This is deprecated, use job_builder section instead
#ignore_cache=True

#[plugin "hipchat"]
#authtoken=dummy

#[plugin "stash"]
#username=user
#password=pass

jenkins-jobs --conf jenkins_jobs.ini update managed_jobs/JavaWS_Management.yml
```

- When you need change job config, simply change YAML job config file and user jenkins-job update command to update your jenkins job in jenkins server. Example:

```bash
vim  managed_jobs/JavaWS_Management.yml
// modify YAML job config file
// then update job by pass updated YAML job config file
jenkins-jobs --conf jenkins_jobs.ini update managed_jobs/JavaWS_Management.yml
```

This work can be automated by jenkins itself and git.