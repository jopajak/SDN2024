from mininet.topo import Topo

class Topology (Topo):

	def __init__(self):
	
		Topo.__init__(self)
	
		#Elements for our topology
		lewyhost = self.addHost("h1")
		prawyhost = self.addHost("h2")
		lewySwitch = self.addSwitch("s1")
		dolSwitch = self.addSwitch("s2")
		prawySwitch = self.addSwitch("s3")
		goraSwitch = self.addSwitch("s4")
	
		#Links in our topology
		self.addLink(lewyhost, lewySwitch, cls=TCLink, bw=100)
		self.addLink(lewySwitch, dolSwitch, cls=TCLink, bw=100)
		self.addLink(lewySwitch, goraSwitch, cls=TCLink, bw=100)
		self.addLink(dolSwitch, prawySwitch, cls=TCLink, bw=100)
		self.addLink(prawySwitch, prawyhost, cls=TCLink, bw=100)
		self.addLink(goraSwitch, prawySwitch, cls=TCLink, bw=100)

topos = {'mytopo': (lambda: Topology() ) }

# sudo mn --custom /home/floodlight/Desktop/Projekt_SDN/Topo.py --topo mytopo --link=tc
	
