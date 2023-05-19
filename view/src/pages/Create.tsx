import {
  Box,
  Button,
  Checkbox,
  FormControl,
  FormHelperText,
  FormLabel,
  HStack,
  Input,
  InputGroup,
  InputLeftAddon,
  NumberDecrementStepper,
  NumberIncrementStepper,
  NumberInput,
  NumberInputField,
  NumberInputStepper,
  Radio,
  RadioGroup,
  Select,
  Stack,
  Textarea
} from '@chakra-ui/react'
import axios from "axios";
import { useEffect, useState } from 'react';
import { Form } from 'react-router-dom'

interface Data {
  gosti: [],
  termini: {},
  pozicije: [],
  usluzniObjekti: []
}

  export default function Create() {
    const [data, setData] = useState();
    const [idGosta, setIdGosta] = useState();
    const [brojMobitelaGosta, setBrojMobitelaGosta] = useState();
    const [idObjekta, setIdObjekta] = useState();
    const [datumRezervacije, setDatumRezervacije] = useState();
    const [brojOsoba, setBrojOsoba] = useState();
    const [pozicija, setPozicija] = useState();
    const [terminRezervacije, setTerminRezervacije] = useState();

    const fetchData =() => {
        axios.get('http://localhost:8081/rezervacije').then((response) => {
          // setData(response.data);
          console.log(response.data);
        });
      }
    
      useEffect(() => {
        fetchData();
      }, []);
  
    
    return (
      <Box maxWidth={'1000px'} justifyContent={'center'}>
        <Form>
            <HStack justifyContent={'space-between'} alignItems={'start'}>
                <Box>
                    <FormControl isRequired mb="40px">
                        <FormLabel>Odaberi gosta:</FormLabel>
                        <Select >
                          <option value={1}>gost</option>
                        </Select>
                    </FormControl>

                    <FormControl mb="40px">
                        <FormLabel>Broj mobitela:</FormLabel>
                        <InputGroup>
                            <InputLeftAddon children='+385' />
                            <Input type='tel' placeholder='phone number'/>
                        </InputGroup>
                        <FormHelperText>U slučaju promjena u rezervaciji.</FormHelperText>
                    </FormControl>

                    <FormControl isRequired mb="40px">
                        <FormLabel>Odaberi objekt:</FormLabel>
                        <Select>
                            <option value='option1'>Boogie Lab Zagreb, Ulica kneza Borne 26, Zagreb</option>
                            <option value='option2'>The Beertija, Ulica Pavla Hatza 16, Zagreb</option>
                            <option value='option2'>Leggiero, Maksimirsko naselje IV 25, Zagreb</option>
                        </Select>
                    </FormControl>

                    <FormControl isRequired mb="40px">
                        <FormLabel>Datum rezervacije:</FormLabel>
                        <Input
                            placeholder="Select Date and Time"
                            size="md"
                            type='date'
                        />
                    </FormControl>
                </Box>
                <Box>
                    <FormControl isRequired display="flex" alignItems="center" mb="40px">
                        <FormLabel>Broj osoba:</FormLabel>
                        <NumberInput defaultValue={1} min={1}>
                            <NumberInputField />
                            <NumberInputStepper>
                                <NumberIncrementStepper />
                                <NumberDecrementStepper />
                            </NumberInputStepper>
                        </NumberInput>
                    </FormControl>

                    <FormControl isRequired mb="40px">
                        <FormLabel>Vrsta stola:</FormLabel>
                        <RadioGroup>
                            <Stack direction='column'>
                                <Radio value='1'>na sredini</Radio>
                                <Radio value='2'>uz prozor</Radio>
                                <Radio value='3'>šank</Radio>
                                <Radio value='4'>separe</Radio>
                                <Radio value='5'>uz prolaz</Radio>
                                <Radio value='6'>terasa</Radio>
                            </Stack>
                        </RadioGroup>
                    </FormControl>

                    <FormControl isRequired mb="40px">
                        <FormLabel>Termin rezervacije:</FormLabel>
                        <RadioGroup>
                            <Stack direction='column'>
                                <Radio value='1'>10:00 - 11:30</Radio>
                                <Radio value='2'>11:30 - 13:00</Radio>
                                <Radio value='3'>13:00 - 14:00</Radio>
                                <Radio value='4'>14:00 - 15:00</Radio>
                                <Radio value='5'>15:00 - 16:30</Radio>
                            </Stack>
                        </RadioGroup>
                    </FormControl>
                </Box>
            </HStack>      
        
  
          <Button size='md'
            height='48px'
            width='200px'
            border='1px' 
            type="submit">
                submit
            </Button>
        </Form>
      </Box>
    );
  }