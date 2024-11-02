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
		self.addLink(lewyhost, lewySwitch)
		self.addLink(lewySwitch, dolSwitch)
		self.addLink(lewySwitch, goraSwitch)
		self.addLink(dolSwitch, prawySwitch)
		self.addLink(prawySwitch, prawyhost)
		self.addLink(goraSwitch, prawySwitch)

topos = {'mytopo': (lambda: Topology() ) }

# sudo mn --custom /home/floodlight/Desktop/Projekt_SDN/Topo.py --topo mytopo
	
