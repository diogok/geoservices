# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  config.vm.box = "ubuntu/trusty64"

  config.vm.network "private_network", ip: "192.168.50.75"

  config.vm.provision "docker" do |d|
    d.run "orchardup/postgresql", name: "postgresql", args: "-p 5432:5432 -v /var/lib/postgresql:/var/lib/postgresql:rw"
  end

  config.vm.provision :shell, :inline => "apt-get update && apt-get install openjdk-7-jdk curl git tmux vim htop postgresql-client -y"
  config.vm.provision :shell, :inline => "wget https://raw.github.com/technomancy/leiningen/stable/bin/lein -O /usr/bin/lein"
  config.vm.provision :shell, :inline => "chmod +x /usr/bin/lein"
end

