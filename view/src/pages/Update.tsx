import { useEffect, useState } from "react";
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
import { Form } from 'react-router-dom'
import { useLocation } from "react-router-dom";

interface Data {
    gosti: [],
    termini: {},
    pozicije: [],
    usluzniObjekti: []
  }


interface FormData {
    idGosta: number,
    brojMobitelaGosta: string,
    idObjekta: number,
    datumRezervacije: string,
    brojOsoba: number,
    pozicija: number,
    terminRezervacije: number
}


export default  function Update() {
    const [data, setData] = useState([]);
    const [form, setForm] = useState<FormData>({
        idGosta: 1,
        brojMobitelaGosta: '',
        idObjekta: 1,
        datumRezervacije: '',
        brojOsoba: 1,
        pozicija: 1,
        terminRezervacije: 1
    })
    
    const {state} = useLocation();
    const { id } = state;

    const fetchRezervacija =() => {
        axios.get('http://localhost:8081/rezervacije/' + id).then((response) => {

          if (response.data != undefined)
            setForm(response.data);
          console.log(form);
        });
      }

    const fetchData =() => {
        axios.get('http://localhost:8081/rezervacije').then((response) => {
          setData(response.data);
          console.log(data);
        });
      }
    
      useEffect(() => {
        console.log(id);
        fetchRezervacija();
        fetchData();
      }, []);

      const handleChange = (e : any) => {
        const {name, value} = e.target;
        setForm({ ...form, [name]: value });
      }

      const handlePozicijaChange = (value : any) => {
        setForm({ ...form, ['pozicija']: value});
      }

      const handleTerminChange = (value : any) => {
        setForm({ ...form, ['terminRezervacije']: value});
      }

      const handleSubmit = async(e : any) => {
        e.preventDefault();
        console.log(form);
        axios.put('http://localhost:8081/rezervacije' + id, form).then(response => {
           console.log(response)
        }) 
      }
  

      return (
        <Box maxWidth={'1000px'} justifyContent={'center'}>
          <Form onSubmit={handleSubmit}>
              <HStack justifyContent={'space-between'} alignItems={'start'}>
                  <Box>
                      <FormControl isRequired mb="40px">
                          <FormLabel>Odaberi gosta:</FormLabel>
                          <Select name='idGosta' value={form.idGosta} onChange={handleChange}>
                            <option value={1}>Ana Anic</option>
                            <option value={2}>Pero Peric</option>
                          </Select>
                      </FormControl>
  
                      <FormControl mb="40px">
                          <FormLabel>Broj mobitela:</FormLabel>
                          <InputGroup>
                              <InputLeftAddon children='+385' />
                              <Input type='tel' placeholder='phone number' name='brojMobitelaGosta' value={form.brojMobitelaGosta} onChange={handleChange}/>
                          </InputGroup>
                          <FormHelperText>U slučaju promjena u rezervaciji.</FormHelperText>
                      </FormControl>
  
                      <FormControl isRequired mb="40px">
                          <FormLabel>Odaberi objekt:</FormLabel>
                          <Select  name='idObjekta' value={form.idObjekta} onChange={handleChange}>
                              <option value={1}>Boogie Lab Zagreb, Ulica kneza Borne 26, Zagreb</option>
                              <option value={2}>The Beertija, Ulica Pavla Hatza 16, Zagreb</option>
                              <option value={3}>Leggiero, Maksimirsko naselje IV 25, Zagreb</option>
                          </Select>
                      </FormControl>
  
                      <FormControl isRequired mb="40px">
                          <FormLabel>Datum rezervacije:</FormLabel>
                          <Input
                              placeholder="Select Date and Time"
                              size="md"
                              type='date'
                              name='datumRezervacije' 
                              value={form.datumRezervacije} onChange={handleChange}
                          />
                      </FormControl>
                  </Box>
                  <Box>
                      <FormControl isRequired display="flex" alignItems="center" mb="40px">
                          <FormLabel>Broj osoba:</FormLabel>
                          <NumberInput min={1}>
                              <NumberInputField name='brojOsoba' 
                              value={form.brojOsoba} onChange={handleChange}/>
                              <NumberInputStepper>
                                  <NumberIncrementStepper />
                                  <NumberDecrementStepper />
                              </NumberInputStepper>
                          </NumberInput>
                      </FormControl>
  
                      <FormControl isRequired mb="40px">
                          <FormLabel>Vrsta stola:</FormLabel>
                          <RadioGroup
                              value={form.pozicija.toString()} onChange={handlePozicijaChange}>
                              <Stack direction='column' >
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
                          <RadioGroup 
                              value={form.terminRezervacije.toString()} onChange={handleTerminChange}>
                              <Stack direction='column' >
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
