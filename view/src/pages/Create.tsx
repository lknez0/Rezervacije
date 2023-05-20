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
    Textarea,
    useToast
  } from '@chakra-ui/react'
  import axios from "axios";
  import { useEffect, useState } from 'react';
  import { Form, useNavigate } from 'react-router-dom'
  
  
  interface FormData {
      idGosta: number,
      brojMobitelaGosta: string,
      idObjekta: number,
      datumRezervacije: string,
      brojOsoba: number,
      pozicija: number,
      terminRezervacije: number
  }

  interface Gost {
    imeKorisnika:string,
    idGosta: number,
    prezimeKorisnika: string
  }

  interface Pozicija {
    idPozicije: number,
    nazivPozicije: string
  }

  interface UsluzniObjekti {
    idObjekta: number,
    nazivObjekta: string,
    adresaObjekta: string,
    gradObjekta: string,
    termini: Termin[]
  }


  interface Termin {
    idTermina: number,
    vrijemePocetka: string,
    vrijemeZavrsetka: string
  }

  
    export default function Create() {
      const navigate = useNavigate();
      const toast = useToast();
      const [gosti, setGosti] = useState<Gost[]>([]);
      const [pozicije, setPozicije] = useState<Pozicija[]>([]);
      const [usluzniObjekti, setUsluzniObjekti] = useState<UsluzniObjekti[]>([]);
      const [termini, setTermini] = useState<Termin[]>([]);
  
      const [form, setForm] = useState<FormData>({
          idGosta: 1,
          brojMobitelaGosta: '',
          idObjekta: 1,
          datumRezervacije: '',
          brojOsoba: 1,
          pozicija: 1,
          terminRezervacije: 1
      })
  
      const fetchData =() => {
          axios.get('http://localhost:8081/rezervacije').then((response) => {
            // setData(response.data);
            console.log(response.data);

            if (response.data.gosti != undefined)
                setGosti(response.data.gosti)

            if (response.data.pozicije != undefined)
                setPozicije(response.data.pozicije)

            if (response.data.usluzniObjekti != undefined) 
                setUsluzniObjekti(response.data.usluzniObjekti)
            
                
            setTermini(response.data.usluzniObjekti[0].termini)
            
          });
        }
      
        useEffect(() => {
          fetchData();
        }, []);


        const handleChange = (e : any) => {
          const {name, value} = e.target;
          setForm({ ...form, [name]: value });
        }
  
        const handleInputChange = (e : any) => {
          const {name, value} = e.target;
  
          setForm({ ...form, [name]: parseInt(value) });
  
          if (name == "idObjekta") {
            usluzniObjekti.forEach(objekt => {
            if (objekt.idObjekta == value) 
                 setTermini(objekt.termini)
             });
           }
           console.log(termini);
        }
  
        const handlePozicijaChange = (value : any) => {
          setForm({ ...form, ['pozicija']: parseInt(value)});
        }
  
        const handleTerminChange = (value : any) => {
          setForm({ ...form, ['terminRezervacije']: parseInt(value)});
        }
  
        const handleSubmit = async(e : any) => {
          e.preventDefault();
          console.log( form );
          const data = new FormData();
          data.append('data', new Blob([JSON.stringify(form)], { type: 'application/json'}));
          axios.post('http://localhost:8081/rezervacije', data, {headers: {
            "Content-Type": "application/json"}
          }).then(response => {
             console.log(response)
             navigate('/');
          }).catch(res => {
            console.log(res);
            toast({
              title: 'Pogreška pri kreiranju rezervacije',
              description: res.data,
              status: 'error',
              duration: 9000,
              isClosable: true
            });
          }); 
        }
      
      return (
        <Box maxWidth={'1000px'} justifyContent={'center'}>
          <Form onSubmit={handleSubmit}>
              <HStack justifyContent={'space-between'} alignItems={'start'}>
                  <Box>
                      <FormControl isRequired mb="40px">
                          <FormLabel>Odaberi gosta:</FormLabel>
                          <Select name='idGosta' value={form.idGosta} onChange={handleInputChange}>
                            {gosti && gosti.map((gost) => (
                                <option key={gost.idGosta} value={gost.idGosta}>{gost.imeKorisnika} {gost.prezimeKorisnika}</option>
                            ))}
                          </Select>
                      </FormControl>
  
                      <FormControl mb="40px">
                          <FormLabel>Broj mobitela:</FormLabel>
                          <InputGroup>
                              <InputLeftAddon children='+385' />
                              <Input type='tel' placeholder='phone number' maxLength={10} name='brojMobitelaGosta' value={form.brojMobitelaGosta} onChange={handleChange}/>
                          </InputGroup>
                          <FormHelperText>U slučaju promjena u rezervaciji.</FormHelperText>
                      </FormControl>
  
                      <FormControl isRequired mb="40px">
                          <FormLabel>Odaberi objekt:</FormLabel>
                          <Select  name='idObjekta' value={form.idObjekta} onChange={handleInputChange}>
                          {usluzniObjekti && usluzniObjekti.map((objekt) => (
                                <option key={objekt.idObjekta} value={objekt.idObjekta}>{objekt.nazivObjekta}, {objekt.adresaObjekta}, {objekt.gradObjekta}</option>
                            ))}
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
                              {pozicije && pozicije.map((pozicija) => (
                                <Radio value={pozicija.idPozicije.toString()}>{pozicija.nazivPozicije}</Radio>
                            ))}
                              </Stack>
                          </RadioGroup>
                      </FormControl>
  
                      <FormControl isRequired mb="40px">
                          <FormLabel>Termin rezervacije:</FormLabel>
                          <RadioGroup 
                              value={form.terminRezervacije.toString()} onChange={handleTerminChange}>
                              <Stack direction='column' >
                              {termini && termini.map((termin) => (
                                <Radio value={termin.idTermina.toString()}>
                                    {termin.vrijemePocetka} - {termin.vrijemeZavrsetka}</Radio>
                            ))}
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