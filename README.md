# SDN2024

Projekt __"QoS for a specific type of data traffic"__ realizowany w ramach przedmiotu _Sieci Sterowane Programowo_.

Realizowany przez:
* Jakub Dobosz
* Joanna Pająk
* Magdalena Podsiadło

## Topologia sieci

![topologia_my_topo](https://github.com/user-attachments/assets/327822bb-de18-4d74-846b-41ffae9960f8)

Komenda uruchomienia topologii:
_sudo mn --custom topology.py --topo mytopo_


Generacja ruchu dla topologii podstawowej:\
* sudo apt-get install hping3 -y # zainstaluj program Hping3\
*sudo mn*\
*xterm h1 h2*\
Uruchomienie skryptów w konsoli hostów:\
Host h1: *bash h1.sh*\
Host h2: *bash h2.sh*

## Diagram pseudokodu:

![obraz](https://github.com/user-attachments/assets/42db7d91-e2b7-48a6-a4ca-f68a27530ea5)


