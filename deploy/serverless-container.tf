terraform {
  required_providers {
    yandex = {
      source = "yandex-cloud/yandex"
    }
  }
}
  
provider "yandex" {
  token  =  var.OAuth-token
  cloud_id  = var.cloud-id
  folder_id = var.folder-id
  zone = var.zone
}

resource "yandex_iam_service_account" "serverless-shortener-terraform" {
   name        = "serverless-shortener-terraform"
   description = "serverless containers editor"
   folder_id   = var.folder-id
}

resource "yandex_resourcemanager_folder_iam_member" "admin-account-iam" {
  folder_id   = var.folder-id
  role        = "serverless-containers.editor"
  member      = "serviceAccount:${yandex_iam_service_account.serverless-shortener-terraform.id}"
}

resource "yandex_serverless_container" "museum-bot-container-terraform" {
  name               = "museum-bot-container-terraform"
  memory             = 1024
  execution_timeout  = "30s"
  cores              = 1
  core_fraction      = 5
  concurrency        = 16
  service_account_id = yandex_iam_service_account.serverless-shortener-terraform.id
  image {
    url = "cr.yandex/${var.container-registry-id}/${var.name-docker-image}:latest"
    environment = {
          MYSQL_HOST = "rc1d-a52oz71zmxivxtvu.mdb.yandexcloud.net"
          BOT_TOKEN = "7347649665:AAFdXp4emBGUv5DXWN5ygN21xrO8jL6p0js"
          BOT_NAME = "MuseumBot3.0"
          MYSQL_NAME = "user1"
          MYSQL_PASSWORD = "user1user1"
      }
  }
}
