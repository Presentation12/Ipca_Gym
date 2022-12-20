using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;
using LayerDAL.Services;

namespace LayerBLL.Logics
{
    public class HorarioFuncionarioLogic
    {
        public static async Task<Response> GetHorarioFuncionarioLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<HorarioFuncionario> horarioFuncionarioList = await HorarioFuncionarioService.GetHorarioFuncionariosService(sqlDataSource);
            
            if (horarioFuncionarioList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult(horarioFuncionarioList);
            }
            
            return response;
        }
    }
}
