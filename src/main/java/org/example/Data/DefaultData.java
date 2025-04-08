package org.example.Data;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DefaultData extends AbstractData{


    public DefaultData() throws IOException {
        super();
    }

    public String get_data() {
        return super.get_data();
    }

}