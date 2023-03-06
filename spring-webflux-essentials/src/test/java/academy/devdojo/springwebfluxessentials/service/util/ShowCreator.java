package academy.devdojo.springwebfluxessentials.service.util;

import academy.devdojo.springwebfluxessentials.domain.Show;

public class ShowCreator {

    public static Show toBeSaved(){
        return Show.builder()
                .name("testShow")
                .build();
    }

    public static Show validShow(){
        return Show.builder()
                .id(1)
                .name("testShow")
                .build();
    }

    public static Show toUpdateShow(){
        return Show.builder()
                .id(1)
                .name("testShowToUpdate")
                .build();
    }

}
