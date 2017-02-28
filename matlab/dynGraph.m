classdef dynGraph < handle
    %GRAPH This is the class for creating and managing a dynamic graph editor for Matlab 
    %do not confuse with graph() from matlab code.
    
    properties
        nodes = [];
        arcs = [];
    end
    
    methods
        
        function out = varname(var)
            out = inputname(1);
        end
        
        %Placeholder: G's name should be aquired automatically.
        function G = draw(G,name)
            path = which('dynGraph');
            path = strrep(path, '.m', '.jar');
            javaaddpath(path);
            savepath
            javaObjectEDT('pidr.app.Application', name);
        end
        
        % To add multiple nodes, enter G = G.addNode(arrayOfNodes[])
        % If a single node can not be added, no nodes will be
        function G = addNode(G, nodeID)
            if size(G.nodes) > 0
                for c = nodeID 
                    if any(G.nodes == c)
                        error('Node with ID %d already exists\n', c);
                    end
                end
            end
            G.nodes = [G.nodes, nodeID];
            fprintf('Node added\n');
        end
        
        % To remove multiple nodes, enter G.removeNode(arrayOfNodes[])
        % If a single node can not be removed, no nodes will be
        function G = removeNode(G, nodeID)
            if size(G.nodes) > 0
                for c = nodeID 
                    if ~any(G.nodes == c)
                        error('Node with ID %d does not exist\n', c);
                    end
                end
            end
            for c = nodeID
                pos = find(G.nodes == c);
                G.nodes(pos) = [];
                if size(G.arcs) > 0
                    pos2 = [find(G.arcs(:,2) == c) ; find(G.arcs(:,3)== c)];
                    delete = G.arcs(pos2,1);
                    G.removeArc(delete);
                end
                fprintf('Node %d removed\n', c);
            end
        end
  
        
        % NodeID1 is the head of the arrow if directed equals 1
        % Directed and colorID are optional and set to 0 (not directed, black) by default
        % To add multiple arcs, the arguments must be columns
        % If a single arc can not be added, none will be
        % nodes at the head and tail of the arrow must exist
        function G = addArc(G, arcID, nodeID1, nodeID2, directed, colorID)
            s = size(arcID);
            uniqueID = unique(arcID);
            sU = size(uniqueID);
            
            if ~(sU(1) == s(1))
                error('two arcs with the same ID in parameters\n')
            end

            if nargin < 5
                directed = zeros(s(1),1);
            end
            
            if nargin < 6
                colorID = zeros(s(1),1);
            end
            
            if max(directed) > 1
                error('directed must be boolean (0 or 1)\n')
            end
            
            for c = nodeID1.'
                if ~any(G.nodes(1,:) == c)
                    error('head node %d does not exist\n', c)
                end
            end
            
            for c = nodeID2.'
                if ~any(G.nodes(1,:) == c)
                    error('tail node %d does not exist\n', c)
                end
            end
            
            for c = arcID.' 
                if size(G.arcs) > 0 
                    if any(G.arcs(:,1) == c)
                        error('Arc with ID %d already exists\n', c);
                    end
                end
            end
            G.arcs = [G.arcs; arcID, nodeID1, nodeID2, directed, colorID];
            fprintf('Arc added\n');
        end
        
        %This uses an arc matrix of the same format as arcs[] and tries to 
        %add the arcs in arcs[]. See addArc() description for more.
        function G = addArcs(G, arcMatrix, int)
            M = arcMatrix;
            [s1, s2] = size(M);
            if s2 > 5
                M = vec2mat(M,int);
                M = M.';
                [s1, s2] = size(M);
            end
            if 3 <= s2 && s2 <= 5
                if s2 == 3
                    M = [M, zeros(s(1),1)];
                end
                if s2 <= 4
                    M = [M, zeros(s(1),1)];
                end
                G.addArc(M(:,1),M(:,2),M(:,3),M(:,4),M(:,5))
            else
                error('arcMatrix must have between 3 and 5 columns\n')
            end
        end
        
        % To remove multiple arcs, the arguments must be a column
        % If said column contains the same ID multiple times, only one 
        % arc will be deleted, even if the method prints multiple lines
        % If a single arc can not be added, none will be
        function G = removeArc(G, arcID)
            if size(G.arcs) > 0
                for c = arcID.'
                    if ~any(G.arcs(:,1) == c)
                        error('Arc with ID %d does not exist\n', c);
                    end
                end
            end
            for c = arcID.'
                pos = find(G.arcs(:,1) == c);
                G.arcs(pos,:) = [];
                fprintf('Arc %d removed\n', c);
            end
        end
             
        function [G, nodes, arcs] = extract(G)
            nodes = G.arcs;
            arcs = G.nodes;
        end
        
        function G = clearArcs(G)
            G.arcs = [];
        end
        
        function G = clearNodes(G)
            G.nodes = [];
        end
        
        function G = clearAll(G)
            G.clearArcs();
            G.clearNodes();
        end
        
    end
    
    
end



