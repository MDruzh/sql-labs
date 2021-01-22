# Databricks notebook source
configs = {"fs.azure.account.auth.type": "OAuth",
         "fs.azure.account.oauth.provider.type": "org.apache.hadoop.fs.azurebfs.oauth2.ClientCredsTokenProvider",
         "fs.azure.account.oauth2.client.id": "131a35ab-36ce-46cc-b5f0-ee92e2cf9903",
         "fs.azure.account.oauth2.client.secret": "_06iwR6XAEIQ2Ph6-N~d9EY05~w7.R4xg-",
         "fs.azure.account.oauth2.client.endpoint": "https://login.microsoftonline.com/540857fe-3605-46a4-a8fe-866dab25b840/oauth2/token",
         "fs.azure.createRemoteFileSystemDuringInitialization": "true"}

dbutils.fs.mount(
        source = "abfss://newlabcontainer@lab9storageacc.dfs.core.windows.net",
        mount_point = "/mnt/newlabs",
        extra_configs = configs)