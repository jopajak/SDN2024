# SDN2024

Projekt __"QoS for a specific type of data traffic"__ realizowany w ramach przedmiotu _Sieci Sterowane Programowo_.

Realizowany przez:
* Jakub Dobosz
* Joanna Pająk
* Magdalena Podsiadło

## Opis projektu 

Celem projektu jest zademonstrowanie działania priorytetyzacji przepływów opartej na numerach portów warstwy czwartej TCP/IP. 
Sieć składa się z czterech przełączników (dokładna topologia poniżej) i dwóch urządzeń końcowych. W sieci zdefiniowaliśmy dwa rodzaje przepływów - ruch priorytetowy voice i ruch tła. W sytuacji, gdy w sieci nie ma natłoku, pakiety głosowe są wysyłane współdzielonym łączem. Gdy szybkość transmisji wzrośnie powyżej 70% maksymalnej przepustowości, nowe przepływy głosowe są kierowane na dedykowane łącze w celu utrzymania pożądanych parametrów QoS.

## Kod kontrolera Floodlight

Repozytorium zawiera klasy kontrolera Floodlight v1.2, które zostały zmienione w ramach naszego projektu.

### Pełny kod źródłowy
Pełny kod źródłowy kontrolera Floodlight v1.2, który można bezpośrednio rozpakować i uruchomić, znajduje się pod adresem:  
[Pełny kod źródłowy Floodlight v1.2](https://drive.google.com/drive/folders/1qnSGO91XvutRrXoLWzKFYeglNx3ZJCt6?usp=sharing)  
*(Nie mógł być umieszczony na GitHubie, ponieważ przekracza maksymalny dozwolony rozmiar pliku).*

### Zmodyfikowane pliki
Dokonywaliśmy zmian jedynie w plikach, które znajdują się w folderze: \src\main\java\pl\edu\agh\kt w kodzie z laboratorium 7. W związku z tym, w repozytorium zamieściliśmy wszystkie klasy z tego miejsca w folderze **`Floodlight-SDN-Code`**.

## Topologia sieci

![topologia_my_topo](https://github.com/user-attachments/assets/327822bb-de18-4d74-846b-41ffae9960f8)

Komenda uruchomienia topologii:
_sudo mn --custom Topo.py --topo mytopo_


Generacja ruchu dla topologii podstawowej:\
*sudo apt-get install hping3 -y # zainstaluj program Hping3\
*sudo mn*\
*xterm h1 h2*\
Uruchomienie skryptów w konsoli hostów:\
Host h1: *bash h1.sh*\
Host h2: *bash h2.sh*

## Diagram pseudokodu:

![obraz](https://github.com/user-attachments/assets/fb100162-14c6-4c15-a4eb-85d2a875b2d1)



