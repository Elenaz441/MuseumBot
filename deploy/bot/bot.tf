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

resource "yandex_container_registry" "my-reg" {
  name = "registry-bot"
  folder_id = "${var.folder-id}"
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
    url = "cr.yandex/${yandex_container_registry.my-reg.id}/${var.name-docker-image}:latest"
    environment = {
          MYSQL_HOST = "rc1d-a52oz71zmxivxtvu.mdb.yandexcloud.net"
          BOT_TOKEN = "7347649665:AAFdXp4emBGUv5DXWN5ygN21xrO8jL6p0js"
          BOT_NAME = "MuseumBot3.0"
          MYSQL_NAME = var.username_db
          MYSQL_PASSWORD = var.password_db
      }
  }
}

resource "yandex_iam_service_account" "service-account-for-museum-bot-terraform" {
   name        = "service-account-for-museum-bot-terraform"
   folder_id   = var.folder-id
}

resource "yandex_resourcemanager_folder_iam_member" "admin-account-iam" {
  folder_id   = var.folder-id
  role        = "editor"
  member      = "serviceAccount:${yandex_iam_service_account.service-account-for-museum-bot-terraform.id}"
}

resource "yandex_api_gateway" "api-gateway" {
  name = "api-gateway-bot"
  connectivity {
    network_id = yandex_vpc_network.network-1.id
  }
  spec = <<-EOT
openapi: 3.0.0
info:
  title: Sample API
  version: 1.0.0
  # servers:
  # - url: 
servers:
- url: https://d5d4v59mthrrsj7p14ec.apigw.yandexcloud.net
paths:
  /:
    get:
      x-yc-apigateway-integration:
        type: dummy
        content:
          '*': '{"msg":"Hello, World! This is gateway."}'
        http_code: 200
        http_headers:
          Content-Type: application/json
    post:
      x-yc-apigateway-integration:
        type: serverless_containers
        container_id: "${var.container-registry-id}"
        service_account_id: "${yandex_iam_service_account.service-account-for-museum-bot-terraform.id}"
EOT
}