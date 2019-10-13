import json
from collections import defaultdict
from typing import Dict, Optional, List, Tuple


class Node(object):
    """Node wrapper class used to stored in the Graph class below
    Assume each node has the unique ident
    """
    def __init__(self, ident: int, attrs: Dict):
        self.ident = ident
        self.attrs = dict(attrs)


class Graph(object):
    """Graph class (undirected) implemented by adjacency list (using set instead of linked list)

    ADT Functions Requirements from CS 225 at UIUC
    https://courses.engr.illinois.edu/cs225/sp2018/lectures/handouts/cs225sp18-33-GraphImpl-handout.pdf

    It contains a dictionary 'nodes' from ident to Node objects in the Graph,
    and a dictionary 'edges' from ident to set of idents, where each ident represents a Node object

    It also keeps track of an increasing number of ident and makes sure each Node it generates
    has a unique ident

    NOTE: Node can only be generated by add_node(), to ensure nodes to have unique idents
    """

    def __init__(self):
        """Constructor of Graph object, setting the first unique ident to be 1
        """
        self.nodes = defaultdict(Node)
        self.edges = defaultdict(set)
        self._next_ident = 1

    def add_node(self, attrs: Dict) -> Node:
        """Adds a node with the given attributes to the graph

        Args:
            attrs: The attributes of the node added

        Returns:
            A node with the given attributes
        """
        node = Node(self._next_ident, attrs)
        self._next_ident += 1
        self.nodes[node.ident] = node
        self.edges.update({node.ident: set()})
        return node

    def add_edge(self, node_1: Node, node_2: Node) -> bool:
        """Adds an edge between the given nodes

        Args:
            node_1: The first node of the edge
            node_2: The second node of the edge

        Returns:
            True if the nodes exist in the graph and the edges are added
            False otherwise
        """
        ident_1, ident_2 = node_1.ident, node_2.ident
        if ident_1 not in self.nodes or ident_2 not in self.nodes:
            return False
        self.edges[ident_1].add(ident_2)
        self.edges[ident_2].add(ident_1)
        return True

    def remove_node(self, node: Node) -> bool:
        """Removes the node from the graph

        Args:
            node: The node to remove

        Returns:
            True if the node exists in the graph and the node is removed
            False otherwise
        """
        ident = node.ident
        if ident not in self.nodes:
            return False

        # Removes it from nodes
        self.nodes.pop(ident)

        # Removes it from edges
        for ident_adj in self.edges[ident]:
            self.edges[ident_adj].remove(ident)
        self.edges.pop(ident)
        return True

    def remove_edge(self, node_1: Node, node_2: Node) -> bool:
        """Removes the edge from the graph, with the given ndoes

        Args:
            node_1: The first node of the edge
            node_2: The second node of the edge

        Returns:
            True if the nodes and the edge exist in the graph and the edge is removed
            False otherwise
        """
        ident_1, ident_2 = node_1.ident, node_2.ident
        # Checks if the nodes and the edge exists
        if ident_1 not in self.nodes or ident_2 not in self.nodes:
            return False
        if ident_2 not in self.edges[ident_1] or ident_1 not in self.edges[ident_2]:
            return False
        self.edges[ident_1].remove(ident_2)
        self.edges[ident_2].remove(ident_1)
        return True

    def adjacent_nodes(self, node: Node) -> Optional[List[Node]]:
        """Gets adjacent nodes of the node

        Args:
            node: The node to search adjacent nodes

        Returns:
            A set of nodes that is adjacent to the node
            None if the node doesn't exist in the graph
        """
        if node.ident not in self.nodes:
            return None
        return [self.nodes[ident_adj] for ident_adj in self.edges[node.ident]]

    def get_nodes(self) -> List[Node]:
        """Gets all the nodes in the graph

        Returns:
            A list of nodes in the graph
        """
        nodes = []
        for _, node in self.nodes.items():
            nodes.append(node)
        return nodes

    def get_edges(self) -> List[Tuple[Node, Node]]:
        """Gets all the edges in the graph
        Enforces the first node has a smaller ident to remove duplicates

        Returns:
            A list of tuples of nodes, indicating the edges in the graph
        """
        edges = []
        for ident, idents_adj in self.edges.items():
            for ident_adj in idents_adj:
                if ident_adj >= ident:
                    continue
                edges.append((self.nodes[ident], self.nodes[ident_adj]))
        return edges

    def store_to_json(self, file_name: str) -> bool:
        """Stores the current data in the graph into a json file

        Args:
            file_name: The file name of the json to store the data

        Returns:
            True if the data is successfully stored in the file named file_name
            False if the file named file_name cannot be opened in writable mode
        """
        nodes_encoded = defaultdict(dict)
        for ident, node in self.nodes.items():
            node_encoded = defaultdict()
            node_encoded.update(node.attrs)
            nodes_encoded.update({ident: node_encoded})  # Resolves that Node is not json-serializable

        edges_encoded = defaultdict(dict)
        for ident, nodes_adj in self.edges.items():
            edges_encoded.update({ident: list(nodes_adj)})  # Resolves that set is not json-serializable

        try:
            with open(file_name, 'w') as f:
                json.dump({
                    "nodes": nodes_encoded,
                    "edges": edges_encoded
                }, f, ensure_ascii=False, indent=4)
                f.close()
        except EnvironmentError:
            return False
        return True

    def read_from_json(self, dict_graph: Dict) -> bool:
        """Reads data from a json object(dictionary in python)

        Args:
            dict_graph: The json object to read

        Returns:
            True if the data is successfully read
            False if some attributes are lost
        """
        self.__init__()
        try:
            # Decodes the edges
            for s_ident, list_adj in dict_graph["edges"].items():
                ident = int(s_ident)
                if ident >= self._next_ident:
                    self._next_ident = ident + 1
                # self.edges.update({ident, set(list_adj)})
                self.edges[ident] = set(list_adj)

            # Decodes the nodes
            for s_ident, attrs in dict_graph["nodes"].items():
                ident = int(s_ident)
                # self.nodes.update({ident, Node(ident, attrs)})
                self.nodes[ident] = Node(ident, attrs)
        except AttributeError:
            return False
        return True

    def read_from_json_file(self, file_name: str) -> bool:
        """Reads data from the json file

        Args:
            file_name: The name of the json file

        Returns:
            True if the data is successfully read from file named file_name
            False otherwise
        """
        try:
            with open(file_name) as f:
                return self.read_from_json(json.load(f))
        except EnvironmentError:
            return False