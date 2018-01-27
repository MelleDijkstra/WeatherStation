import socket

MAX_REQUEST = 5


def start_auto_discovery(port=8888, addr='255.255.255.255'):
    current_request = 1
    # create the socket
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    # make port reusable and specify that we are broadcasting
    s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
    # make the socket multicast-aware, and set TTL.
    s.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, 20)  # change TTL (=20) to suit
    s.settimeout(4)  # wait 4 seconds
    # send the data
    data = "DISCOVER_WEATHERSTATION_REQUEST".encode()
    buf_size = 1024

    while current_request <= MAX_REQUEST:
        # send request
        s.sendto(data, (addr, port))
        print("Request packet (" + str(current_request) + ") sent to: \033[94m" + addr + ":" + str(port) + "\033[0m")

        # wait for response
        try:
            data, server_address = s.recvfrom(buf_size)
            response = data.decode()
            print("Broadcast response from server: " + server_address)
            s.close()
            if response.strip() == "DISCOVER_WEATHERSTATION_RESPONSE":
                return server_address
                # DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
                # test_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                # test_socket.connect((server_address, 3301))
                # print("Connected to the service!")
        except socket.timeout as e:
            print("broadcast timeout for request (" + str(current_request) + ")")
            current_request += 1
    print("\033[93mNo response after " + str(MAX_REQUEST) + " broadcasts, check firewall or if the weatherstation is "
                                                            "actually on!\033[0m")
    return None


if __name__ == '__main__':
    # retrieve ip from autodiscovery
    ip = start_auto_discovery()
    if ip is not None:
        # put the ip in the config file
        pass
