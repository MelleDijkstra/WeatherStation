import socket

def auto_discovery(port=8888, addr='255.255.255.255'):
        # Create the socket
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        # Make the socket multicast-aware, and set TTL.
        s.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, 20) # Change TTL (=20) to suit
        # Send the data
        data = "DISCOVER_WEATHERSTATION_REQUEST".encode()
        s.sendto(data, (addr, port))
        print("Request packet sent to: 255.255.255.255")

        # Wait for response
        buf_size = 1024
        data, server_address = s.recvfrom(buf_size)
        response = data.decode()
        print("Broadcast response from server: " + server_address)
        if response.strip() == "DISCOVER_WEATHERSTATION_RESPONSE":
            # DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
            test_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            test_socket.connect((server_address, 3301))
            print("Connected to the service!")
        s.close()





