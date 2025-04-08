package org.example.Graph;

import org.example.Data.DurakVerileri.StopData;
import org.example.Data.DurakVerileri.TransferData;

public interface ITransferGraphBuilder {
    Graph buildGraph(TransferData transferData);
    String get_name();
}
