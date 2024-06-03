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

resource "yandex_mdb_mysql_user" "user1" {
    cluster_id = yandex_mdb_mysql_cluster.museum-database.id
    name       = "user1"
    password   = "user1user1"

  permission {
    database_name = yandex_mdb_mysql_database.user1.name
    roles = ["ALL"]
  }
}

resource "yandex_mdb_mysql_database" "user1" {
  cluster_id = yandex_mdb_mysql_cluster.museum-database.id
  name       = "user1"
}

resource "yandex_mdb_mysql_cluster" "museum-database" {
  name        = "museum-database"
  environment = "PRODUCTION"
  network_id  = yandex_vpc_network.network-1.id
  version     = "5.7"

  resources {
    resource_preset_id = "s2.micro"
    disk_type_id       = "network-ssd"
    disk_size          = 16
  }

  host {
    zone = "ru-central1-a"
    subnet_id = yandex_vpc_subnet.subnet-1.id
  }
}

output "permission" {
  value = "${yandex_mdb_mysql_user.user1.permission}"
}

resource "yandex_vpc_network" "network-1" {
  name = "terraform-network"
}
 
resource "yandex_vpc_subnet" "subnet-1" {
  name           = "terraform-subnet"
  zone           = "ru-central1-a"
  network_id     = "${yandex_vpc_network.network-1.id}"
  v4_cidr_blocks = ["10.2.0.0/16"]
}