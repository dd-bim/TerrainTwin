import React from 'react';
import { Grid, Paper } from '@mui/material';
import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import { fetchJson } from '../backend_conn';
import useStyles from '../Styles';

export default function RepositoryInput({ repository, setRepository }) {
    const classes = useStyles();
    const [repos, setRepos] = React.useState([""]);

    React.useEffect(() => {
        const response = async () => {;
            const res = await fetchJson("/repositories");
            setRepos(res);
        }
        response();
    }, []);

    function handleChange(event) {
        setRepository(event.target.value);
    };

    return (
        <div className={classes.maxWidth}>
            <Grid container spacing={1} columns={8} className={classes.root}>
                <Grid item sm={2} >
                    <Paper className={classes.text} style={{ lineHeight: "50px" }}>
                        GraphDB Repository
                    </Paper>
                </Grid>
                <Grid item sm={1}>
                <Box style={{width: '100%'}}>
                    <FormControl fullWidth>
                        <InputLabel id="repoLabel">Repository</InputLabel>
                        <Select
                            name="elValue"
                            label="GraphDB Repository"
                            onChange={handleChange}>
                            {
                                repos.map(item => {
                                    return <MenuItem value={item}>{item}</MenuItem>
                                })
                            }
                        </Select>
                    </FormControl>
                    </Box>
                </Grid>
            </Grid>
        </div>
    )
}