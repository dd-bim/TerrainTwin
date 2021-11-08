import * as React from 'react';
import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import { fetchJson, sendParam } from '../backend_conn';

export default function DropdownButton(props) {
  const [items, setItems] = React.useState([""]);

  React.useEffect(() => {
    if (props.repository !== null){
    if (props.initValue !== "" && props.id === 1) {
    const response = async () => {
      const param = 'subjectModel=' + props.initValue;
      const results = await sendParam("POST", "/gruppenimfachmodell/repo/" + props.repository, param);
      setItems(results);
    }
    response();
  } else  if (props.initValue !== "" && props.id === 2) {
    const response = async () => {
      const param = 'group=' + props.initValue;
      const results = await sendParam("POST", "/klasseningruppe/repo/" + props.repository, param);
      setItems(results);
    }
    response();
  } else  if (props.id === 5 || props.id === 7) {
    if (props.initValue !== "") {
    const response = async () => {
      const param = 'relation=' + props.initValue;
      const results = await sendParam("POST", "/predicatesfromrelation/repo/" + props.repository, param);
      setItems(results);
    }
    response();
  } else {}
  } else {
    const response = async () => {
      const results = await fetchJson("/"+ props.elValue.tag + "/repo/" + props.repository);
      setItems(results);
    }
    response();
  }
}
  }, [props]);


  function handleChange(event) {
    props.onChange(props.id, { tag: props.elValue.tag, name: props.elValue.name, value: event.target.value });
  };

  return (
    <Box style={{width: '100%'}}>
      <FormControl fullWidth>
        <InputLabel id="datacatLabel">{props.elValue.name}</InputLabel>
        <Select
          name="elValue"
          label={props.elValue.name}
          onChange={handleChange}>
          {
            items.map(item => {
              const list= item.split("/");
              const element = list[list.length - 1];
              return <MenuItem value={item}>{element}</MenuItem>
            })
          }
        </Select>
      </FormControl>
    </Box>
  );
}